from flask import Flask, request, jsonify, redirect
from dtos import AclEntryDTO
from services import namespace as namespace_service
import json
from flask_limiter import Limiter
import services.acl as acl
from services.api_key import generate_api_key, require_api_key
from flask_limiter.util import get_remote_address
from dotenv import load_dotenv
from services.logger import security_logger, app_logger, sanitize_input, log_request, EncryptedLogFormatter
from exceptions.madagascar_exception import MadagascarException
import os
from flask_talisman import Talisman

load_dotenv()

app = Flask(__name__)
# Talisman(
#     app,
#     force_https=True
# )

logger_encryptor = EncryptedLogFormatter()

def redirect_to_https():
    pass

@app.before_request
def before_request():
    log_request(request, logger_encryptor)
    
    
limiter = Limiter(
    get_remote_address,
    app=app,
    default_limits=["200 per minute"]
)

@app.errorhandler(429)
def too_many_requests(e):
    return jsonify({"message": "Rate limit exceeded. Please try again later."}), 429

@app.route('/healthcheck', methods=['GET'])
@require_api_key
def get_books():
    return "Hello from Madagascar!"

@app.route('/namespace', methods=['POST'])
@require_api_key
def add_namespace():
    data = sanitize_input(request.json)
    try:
        namespace_service.add(data)
        app_logger.info(f"Namespace config added: {logger_encryptor.encrypt(json.dumps(data))}")
        return jsonify({}), 200
    except:
        security_logger.warning(f"Bad request from IP: {request.remote_addr}")
        return jsonify({'Bad request!'}), 400

@app.route('/namespace', methods=['GET'])
@require_api_key
def get_config():
    namespace = sanitize_input(request.args.get('namespace'))
    try:
        value = namespace_service.get(namespace)
        if not value:
            return jsonify({'error': f'Configuration for namespace "{logger_encryptor.encrypt(json.dumps(namespace))}" not found'}), 404
        
        app_logger.info(f"Namespace config fetched")
        return jsonify(value), 200
    except MadagascarException as me:
        security_logger.warning(f"Server error: {str(me)} from IP: {request.remote_addr}")
        return jsonify({'error': str(me)}), 500
    except Exception as e:
        security_logger.warning(f"Server error: {str(e)} from IP: {request.remote_addr}")
        return jsonify({'error': "Internal server error"}), 500

@app.route('/acl', methods=['POST'])
@require_api_key
def add_acl_entry():
    data = sanitize_input(request.json)

    try:
        acl_entry = AclEntryDTO.from_dict(data)
        acl.add(acl_entry)
        app_logger.info(f"ACL entry added: {logger_encryptor.encrypt(str(acl_entry))}")
        return jsonify({'message': 'ACL entry added successfully', 'entry': acl_entry.to_dict()}), 200
    
    except KeyError:
        security_logger.warning(f"Invalid JSON format from IP: {request.remote_addr}")
        return jsonify({'error': 'Invalid JSON format'}), 400
    except MadagascarException as me:
        security_logger.warning(f"Server error: {str(me)} from IP: {request.remote_addr}")
        return jsonify({'error': str(me)}), 500
    except Exception as e:
        security_logger.warning(f"Server error: {str(e)} from IP: {request.remote_addr}")
        return jsonify({'error': "Internal server error."}), 500

@app.route('/acl', methods=['PUT'])
@require_api_key
def update_acl_entry():
    data = sanitize_input(request.json)

    try:
        acl_entry = AclEntryDTO.from_dict(data)
        acl.add(acl_entry, True)
        app_logger.info(f"ACL entry updated: {logger_encryptor.encrypt(str(acl_entry))}")
        return jsonify({'message': 'ACL entry updated successfully', 'entry': acl_entry.to_dict()}), 200
    
    except KeyError:
        security_logger.warning(f"Invalid JSON format from IP: {request.remote_addr}")
        return jsonify({'error': 'Invalid JSON format'}), 400
    except MadagascarException as me:
        security_logger.warning(f"Server error: {str(me)} from IP: {request.remote_addr}")
        return jsonify({'error': str(me)}), 500
    except Exception as e:
        security_logger.warning(f"Server error: {str(e)} from IP: {request.remote_addr}")
        return jsonify({'error': "Internal server error."}), 500

@app.route('/acl/check', methods=['POST'])
@require_api_key
def check_acl_entry():
    data = sanitize_input(request.json)
    
    try:
        acl_entry = AclEntryDTO.from_dict(data)
        response = acl.check(acl_entry)
        app_logger.info(f"ACL check: {logger_encryptor.encrypt(str(acl_entry))} - Authorized: {response}")
        return jsonify({'authorized': response}), 200
    except KeyError:
        security_logger.warning(f"Invalid JSON format from IP: {request.remote_addr}")
        return jsonify({'error': 'Invalid JSON format'}), 400
    except MadagascarException as me:
        security_logger.warning(f"Server error: {str(me)} from IP: {request.remote_addr}")
        return jsonify({'error': str(me)}), 500
    except Exception as e:
        security_logger.warning(f"Server error: {str(e)} from IP: {request.remote_addr}")
        return jsonify({'error': "Internal server error."}), 500
    
@app.route('/acl', methods=['DELETE'])
@require_api_key
def delete_acl_entry():
    data = sanitize_input(request.json)

    try:
        acl_entry = AclEntryDTO.from_dict(data)
        acl.delete(acl_entry)
        app_logger.info(f"ACL entry deleted: {logger_encryptor.encrypt(str(acl_entry))}")
        return jsonify({'message': 'ACL entry deleted successfully', 'entry': acl_entry.to_dict()}), 200
    except KeyError:
        security_logger.warning(f"Invalid JSON format from IP: {request.remote_addr}")
        return jsonify({'error': 'Invalid JSON format'}), 400

@app.route('/api-key', methods=['GET'])
def get_app_key():
    app_key = generate_api_key()
    app_logger.info(f"Generated api key for {request.url}")
    return jsonify({'message': 'Api-Key created.', 'api-key': app_key}), 201

@app.route('/shared/<prefix>', methods=['GET'])
def get_all_relations_for_doc(prefix):
    acls = acl.get_all_relations_for_doc(prefix)
    return jsonify(acls), 200


if __name__ == '__main__':
    # cert_folder = os.getenv('CERT_FOLDER')
    # ssl_context = (
    #     os.path.join(cert_folder, 'cert.pem'), 
    #     os.path.join(cert_folder, 'key.pem')
    # )
    # app.run(host='127.0.0.1', port=4000, ssl_context=ssl_context)
    app.run(host='127.0.0.1', port=4000)
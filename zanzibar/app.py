from flask import Flask, request, jsonify
from dtos import AclEntryDTO
from services import namespace as namespace_service

import services.acl as acl
from services.api_key import generate_api_key, require_api_key

from services.logger import security_logger, app_logger, sanitize_input, log_request

app = Flask(__name__)

@app.before_request
def before_request():
    log_request(request)

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
        app_logger.info(f"Namespace config added: {data}")
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
            return jsonify({'error': f'Configuration for namespace "{namespace}" not found'}), 404
        
        app_logger.info(f"Namespace config fetched")
        return jsonify(value), 200
    except Exception as e:
        security_logger.warning(f"Server error: {str(e)} from IP: {request.remote_addr}")
        return jsonify({'error': str(e)}), 500

@app.route('/acl', methods=['POST'])
@require_api_key
def add_acl_entry():
    data = sanitize_input(request.json)

    try:
        acl_entry = AclEntryDTO.from_dict(data)
        acl.add(acl_entry)
        app_logger.info(f"ACL entry added: {acl_entry}")
        return jsonify({'message': 'ACL entry added successfully', 'entry': acl_entry.to_dict()}), 200
    
    except KeyError:
        security_logger.warning(f"Invalid JSON format from IP: {request.remote_addr}")
        return jsonify({'error': 'Invalid JSON format'}), 400
    except Exception as e:
        security_logger.warning(f"Server error: {str(e)} from IP: {request.remote_addr}")
        return jsonify({'error': str(e)}), 500

@app.route('/acl', methods=['PUT'])
@require_api_key
def update_acl_entry():
    data = sanitize_input(request.json)

    try:
        acl_entry = AclEntryDTO.from_dict(data)
        acl.add(acl_entry, True)
        app_logger.info(f"ACL entry updated: {acl_entry}")
        return jsonify({'message': 'ACL entry updated successfully', 'entry': acl_entry.to_dict()}), 200
    
    except KeyError:
        security_logger.warning(f"Invalid JSON format from IP: {request.remote_addr}")
        return jsonify({'error': 'Invalid JSON format'}), 400
    except Exception as e:
        security_logger.warning(f"Server error: {str(e)} from IP: {request.remote_addr}")
        return jsonify({'error': str(e)}), 500

@app.route('/acl/check', methods=['POST'])
@require_api_key
def check_acl_entry():
    data = sanitize_input(request.json)
    
    try:
        acl_entry = AclEntryDTO.from_dict(data)
        response = acl.check(acl_entry)
        app_logger.info(f"ACL check: {acl_entry} - Authorized: {response}")
        return jsonify({'authorized': response}), 200
    except KeyError:
        security_logger.warning(f"Invalid JSON format from IP: {request.remote_addr}")
        return jsonify({'error': 'Invalid JSON format'}), 400
    except Exception as e:
        security_logger.warning(f"Server error: {str(e)} from IP: {request.remote_addr}")
        return jsonify({'error': str(e)}), 500
    
@app.route('/acl', methods=['DELETE'])
@require_api_key
def delete_acl_entry():
    data = sanitize_input(request.json)

    try:
        acl_entry = AclEntryDTO.from_dict(data)
    except KeyError:
        security_logger.warning(f"Invalid JSON format from IP: {request.remote_addr}")
        return jsonify({'error': 'Invalid JSON format'}), 400
    
    acl.delete(acl_entry)
    app_logger.info(f"ACL entry deleted: {acl_entry}")
    return jsonify({'message': 'ACL entry deleted successfully', 'entry': acl_entry.to_dict()}), 200


@app.route('/api-key', methods=['GET'])
def get_app_key():
    app_key = generate_api_key()
    app_logger.info(f"Generated api key for {request.url}")
    return jsonify({'message': 'Api-Key created.', 'api-key': app_key}), 201


if __name__ == '__main__':
    app.run(host='127.0.0.1', port=4000, debug=True)

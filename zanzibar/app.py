from flask import Flask, request, jsonify
from dtos import AclEntryDTO
from services import namespace as namespace_service
from flask_limiter import Limiter
import services.acl as acl
from services.api_key import generate_api_key, require_api_key
from flask_limiter.util import get_remote_address

app = Flask(__name__)
limiter = Limiter(
    get_remote_address,
    app=app,
    default_limits=["100 per minute"]
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
    data = request.json
    try:
        namespace_service.add(data)
        return jsonify({}), 200
    except:
        return jsonify({'Bad request!'}), 400

@app.route('/namespace', methods=['GET'])
@require_api_key
def get_config():
    namespace = request.args.get('namespace')
    try:
        value = namespace_service.get(namespace)
        if not value:
            return jsonify({'error': f'Configuration for namespace "{namespace}" not found'}), 404
        
        return jsonify(value), 200
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@app.route('/acl', methods=['POST'])
@require_api_key
def add_acl_entry():
    data = request.json

    try:
        acl_entry = AclEntryDTO.from_dict(data)
        acl.add(acl_entry)
        return jsonify({'message': 'ACL entry added successfully', 'entry': acl_entry.to_dict()}), 200
    
    except KeyError:
        return jsonify({'error': 'Invalid JSON format'}), 400
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@app.route('/acl', methods=['PUT'])
@require_api_key
def update_acl_entry():
    data = request.json

    try:
        acl_entry = AclEntryDTO.from_dict(data)
        acl.add(acl_entry, True)
        return jsonify({'message': 'ACL entry updated successfully', 'entry': acl_entry.to_dict()}), 200
    
    except KeyError:
        return jsonify({'error': 'Invalid JSON format'}), 400
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@app.route('/acl/check', methods=['POST'])
@require_api_key
def check_acl_entry():
    data = request.json
    
    try:
        acl_entry = AclEntryDTO.from_dict(data)
    except KeyError:
        return jsonify({'error': 'Invalid JSON format'}), 400
    
    response = acl.check(acl_entry)

    return jsonify({'authorized': response}), 200

@app.route('/acl', methods=['DELETE'])
@require_api_key
def delete_acl_entry():
    data = request.json

    try:
        acl_entry = AclEntryDTO.from_dict(data)
    except KeyError:
        return jsonify({'error': 'Invalid JSON format'}), 400
    
    acl.delete(acl_entry)

    return jsonify({'message': 'ACL entry deleted successfully', 'entry': acl_entry.to_dict()}), 200


@app.route('/api-key', methods=['GET'])
def get_app_key():
    app_key = generate_api_key()
    return jsonify({'message': 'Api-Key created.', 'api-key': app_key}), 201

if __name__ == '__main__':
    app.run(host='127.0.0.1', port=4000, debug=True)

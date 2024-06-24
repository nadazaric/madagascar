from flask import Flask, request, jsonify
from dtos import AclEntryDTO
from services import namespace as namespace_service

import services.acl as acl

app = Flask(__name__)

@app.route('/healthcheck', methods=['GET'])
def get_books():
    return "Hello from Madagascar!"

@app.route('/namespace', methods=['POST'])
def add_namespace():
    data = request.json
    try:
        namespace_service.add(data)
        return jsonify({}), 200
    except:
        return jsonify({'Bad request!'}), 400

@app.route('/namespace', methods=['GET'])
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
def add_acl_entry():
    data = request.json
    print(data)
    try:
        acl_entry = AclEntryDTO.from_dict(data)
        acl.add(acl_entry)
        return jsonify({'message': 'ACL entry added successfully', 'entry': acl_entry.to_dict()}), 200
    
    except KeyError:
        return jsonify({'error': 'Invalid JSON format'}), 400
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@app.route('/acl/check', methods=['GET'])
def check_acl_entry():
    data = request.json

    try:
        acl_entry = AclEntryDTO.from_dict(data)
    except KeyError:
        return jsonify({'error': 'Invalid JSON format'}), 400
    
    response = acl.check(acl_entry)

    return jsonify({'authorized': response}), 200

@app.route('/acl', methods=['DELETE'])
def delete_acl_entry():
    data = request.json

    try:
        acl_entry = AclEntryDTO.from_dict(data)
    except KeyError:
        return jsonify({'error': 'Invalid JSON format'}), 400
    
    acl.delete(acl_entry)

    return jsonify({'message': 'ACL entry deleted successfully', 'entry': acl_entry.to_dict()}), 200

if __name__ == '__main__':
    app.run(host='127.0.0.1', port=4000, debug=True)

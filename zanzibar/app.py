from flask import Flask, request, jsonify
from dtos import AclEntryDTO

import services.acl as acl
from services.api_key import generate_api_key

app = Flask(__name__)

@app.route('/healthcheck', methods=['GET'])
def get_books():
    return "Hello from Madagascar!"

@app.route('/acl', methods=['POST'])
def add_acl_entry():
    data = request.json

    try:
        acl_entry = AclEntryDTO.from_dict(data)
    except KeyError:
        return jsonify({'error': 'Invalid JSON format'}), 400
    
    acl.add(acl_entry)

    return jsonify({'message': 'ACL entry added successfully', 'entry': acl_entry.to_dict()}), 200

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


@app.route('/api-key', methods=['GET'])
def get_app_key():
    app_key = generate_api_key()
    return jsonify({'message': 'Api-Key created.', 'api-key': app_key}), 200

if __name__ == '__main__':
    app.run(debug=True)

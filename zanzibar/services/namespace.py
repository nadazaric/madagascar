import consul
import json
from functools import lru_cache
import logging

# Initialize the Consul client
client = consul.Consul(host='localhost', port=8500)
logging.basicConfig(level=logging.INFO)

def add(data):
    namespace_dict = data

    consul_key = namespace_dict['namespace']
    relations_dict = namespace_dict.get('relations', {})
    consul_value = json.dumps(relations_dict)

    client.kv.put(consul_key, consul_value)
    get.cache_clear()

@lru_cache(maxsize=3)
def get(namespace):
    try:
        index, data = client.kv.get(namespace)
        logging.info(f"Fetching data from Consul for namespace: {namespace}")
        if not data:
            raise KeyError("There is no namespace!")
        
        return json.loads(data['Value'].decode('utf-8'))
    except Exception as e:
        raise Exception("Server error!")
    
# relations is value for key-value pair in consul db
def get_roles_for_role(role, relations):
    def resolve_role(current_role, resolved_roles):
        if current_role in resolved_roles:
            return
        resolved_roles.add(current_role)
        role_relations = relations.get(current_role, {})
        if role_relations and 'union' in role_relations and role_relations['union']:
            for sub_role in role_relations['union']:
                if sub_role != "this":
                    resolve_role(sub_role, resolved_roles)

    resolved_roles = set()
    resolve_role(role, resolved_roles)
    return resolved_roles
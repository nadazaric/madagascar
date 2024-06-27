from typing import Dict, Set
import consul
import json
from functools import lru_cache
from dtos import AclEntryDTO
from services.logger import security_logger, app_logger
from exceptions.madagascar_exception import MadagascarException

# Initialize the Consul client
client = consul.Consul(host='localhost', port=8500)

def add(data):
    namespace_dict = data

    consul_key = namespace_dict['namespace']
    relations_dict = namespace_dict.get('relations', {})
    consul_value = json.dumps(relations_dict)

    try:
        n = get(consul_key)
        consul_value["version"] = n["version"] + 1
        client.kv.put(consul_key+str(consul_value["version"]), consul_value)
    except:
        consul_value["version"] = 1
        client.kv.put(consul_key+str(consul_value["version"]), consul_value)

    client.kv.put(consul_key, consul_value)
    get.cache_clear()

def rollback(namespace):
    n = get(namespace)
    if n["version"] == 1:
        return
    try:
        version_key = namespace + str(n['version']-1)
        index, data = client.kv.get(version_key)
        if not data:
            return None
        client.kv.put(namespace, data)
    except Exception as e:
        raise MadagascarException("Server error!")
    

@lru_cache(maxsize=3)
def get(namespace) -> Dict[str, object]:
    try:
        index, data = client.kv.get(namespace)
        if not data:
            return None
        return json.loads(data['Value'].decode('utf-8'))
    except Exception as e:
        raise MadagascarException("Server error!")

def get_roles(namespace_name: str) -> Set[str]:
    namespace = get(namespace_name)
    if namespace:
        return set(namespace.keys())
    
    return set()
    
# relations is value for key-value pair in consul db
def get_roles_for_role(role, aclObject):
    namespace_name = extract_namespace_name(aclObject)
    relations = get(namespace_name)

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

    resolved_roles.discard(role)

    return resolved_roles

def validate_namespace_acl(acl: AclEntryDTO) -> bool:
    namespace_name = extract_namespace_name(acl.object)
    namespace = get(namespace_name)

    if not namespace:
        return False
    
    if acl.relation not in namespace.keys():
        return False
    
    return True

def extract_namespace_name(aclObject: str) -> str:
    return aclObject.split(":")[0]
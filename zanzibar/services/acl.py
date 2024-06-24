import plyvel
from services.namespace import validate_namespace_acl, get_roles_for_role, get
import copy

from dtos import AclEntryDTO

db_path = './data'

def _get_key(entry: AclEntryDTO) -> bytes:
    return f"{entry.object}#{entry.relation}@{entry.user}".encode()

def add(entry: AclEntryDTO) -> None:
    if not validate_namespace_acl(entry):
        raise Exception("Acl entry not valid.")

    key = _get_key(entry)
    value = b""  

    try:
        db = plyvel.DB('tmp', create_if_missing=True)
        db.put(key, value)
    finally:
        if db is not None:
            db.close()

def check(entry: AclEntryDTO) -> bool:
    #TODO: implement

    key = _get_key(entry)

    try:
        db = plyvel.DB('tmp', create_if_missing=True)
        if db.get(key) is not None:
            return True
    finally:
        if db is not None:
            db.close()

    parent_roles = get_roles_for_role(entry.relation, entry.object)
    print(parent_roles)
    
    try:
        db = plyvel.DB('tmp', create_if_missing=True)
        for role in parent_roles:
            entry.relation = role
            key = _get_key(entry)
            
            if db.get(key) is not None:
                return True
    finally:
        if db is not None:
            db.close()
    
    return False

def delete(entry: AclEntryDTO) -> None:
    key = _get_key(entry)

    try:
        db = plyvel.DB('tmp', create_if_missing=True)
        db.delete(key)
    finally:
        if db is not None:
            db.close()

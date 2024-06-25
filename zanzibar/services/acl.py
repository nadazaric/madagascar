import plyvel
from services.namespace import validate_namespace_acl, get_roles_for_role, get_roles, extract_namespace_name
import copy

from dtos import AclEntryDTO
from config import LEVELDB_NAME

def _get_key(entry: AclEntryDTO) -> bytes:
    return f"{entry.object}#{entry.relation}@{entry.user}".encode()

def delete_existant(entry: AclEntryDTO) -> None:
    roles = get_roles(extract_namespace_name(entry.object))
    roles.discard(entry.relation)

    entry_copy = copy.deepcopy(entry)
    for role in roles:
        entry_copy.relation = role
        delete(entry_copy)

def add(entry: AclEntryDTO, update = False) -> None:
    if not validate_namespace_acl(entry):
        raise Exception("Acl entry not valid.")
    
    if update:
        delete_existant(entry)

    key = _get_key(entry)
    value = b""  

    try:
        db = plyvel.DB(LEVELDB_NAME, create_if_missing=True)
        db.put(key, value)
    finally:
        if db is not None:
            db.close()

def check(entry: AclEntryDTO) -> bool:
    key = _get_key(entry)

    try:
        db = plyvel.DB(LEVELDB_NAME, create_if_missing=True)
        if db.get(key) is not None:
            return True
    finally:
        if db is not None:
            db.close()

    parent_roles = get_roles_for_role(entry.relation, entry.object)
    
    try:
        db = plyvel.DB(LEVELDB_NAME, create_if_missing=True)
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
        db = plyvel.DB(LEVELDB_NAME, create_if_missing=True)
        db.delete(key)
    finally:
        if db is not None:
            db.close()

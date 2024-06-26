import plyvel
from services.namespace import validate_namespace_acl, get_roles_for_role, get_roles, extract_namespace_name
import copy
from exceptions.madagascar_exception import MadagascarException
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
        raise MadagascarException("Acl entry not valid.")
    
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
    if not validate_namespace_acl(entry):
        raise MadagascarException("Acl entry not valid.")
    
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

def get_all_relations_for_doc(prefix):
    try:
        db = plyvel.DB(LEVELDB_NAME, create_if_missing=True)
        prefixed_db = db.prefixed_db(prefix.encode())
        relations = [get_acl_dto_from_relation(prefix + key.decode()).to_dict() for key, _ in prefixed_db]
        return relations
    finally:
        if db is not None:
            db.close()

def get_acl_dto_from_relation(relation):
    left = relation.split("#")[0]
    right = relation.split("#")[1]
    role = right.split("@")[0]
    user = right.split("@")[1]
    acl = AclEntryDTO(left, role, user)
    return acl

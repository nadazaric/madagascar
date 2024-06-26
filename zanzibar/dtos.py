class AclEntryDTO:
    def __init__(self, object, relation, user):
        self.object = object
        self.relation = relation
        self.user = user

    @classmethod
    def from_dict(cls, data):
        return cls(data['object'], data['relation'], data['user'])

    def to_dict(self):
        return {
            'object': self.object,
            'relation': self.relation,
            'user': self.user
        }
    
    def __str__(self):
        return f"AclEntryDTO(object={self.object}, relation={self.relation}, user={self.user})"
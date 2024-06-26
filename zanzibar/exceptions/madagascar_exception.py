class MadagascarException(Exception):
    def init(self, message):
        self.message = message
        super().init(self.message)
package ftn.rbs.madagascar_hub.services.interfaces;

import ftn.rbs.madagascar_hub.models.User;

public interface IUserService {
    User getUserByEmail(String email);
    User getCurrentUser();
}

package controller;

import dao.UserDAO;
import model.User;

/**
 * Controlador que gerencia ações de usuários.
 */
public class UserController {

    private final UserDAO userDAO = new UserDAO();

    /**
     * Cria um usuário no banco.
     */
    public void createUser(User user) {
        userDAO.addUser(user);
    }

    /**
     * Tenta logar o usuário pelo username e senha.
     * @return User autenticado ou null se inválido.
     */
    public User login(String username, String password) {
        return userDAO.authenticate(username, password);
    }
}

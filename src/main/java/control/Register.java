package control;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.DriverManagerConnectionPool;

@WebServlet("/Register")
public class Register extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public Register() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String indirizzo = request.getParameter("indirizzo");
        String telefono = request.getParameter("telefono");
        String carta = request.getParameter("carta");
        String intestatario = request.getParameter("intestatario");
        String cvv = request.getParameter("cvv");
        String redirectedPage = "/loginPage.jsp";

        try {
            Connection con = DriverManagerConnectionPool.getConnection();
            String sql = "INSERT INTO UserAccount(email, passwordUser, nome, cognome, indirizzo, telefono, numero, intestatario, CVV) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            String sql2 = "INSERT INTO Cliente(email) VALUES (?)";
            String sql3 = "INSERT INTO Venditore(email) VALUES (?)";

            // Hash della password con SHA-256
            String hashedPassword = hashPassword(password);

            // Aggiungi a AccountUser
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, hashedPassword); // Utilizza la password crittografata
            ps.setString(3, nome);
            ps.setString(4, cognome);
            ps.setString(5, indirizzo);
            ps.setString(6, telefono);
            ps.setString(7, carta);
            ps.setString(8, intestatario);
            ps.setString(9, cvv);

            ps.executeUpdate();
            con.commit();

            // Aggiungi a Cliente
            PreparedStatement ps2 = con.prepareStatement(sql2);
            ps2.setString(1, email);

            ps2.executeUpdate();
            con.commit();

            // Aggiungi a Venditore
            PreparedStatement ps3 = con.prepareStatement(sql3);
            ps3.setString(1, email);

            ps3.executeUpdate();
            con.commit();
            DriverManagerConnectionPool.releaseConnection(con);
        } catch (SQLException e) {
            request.getSession().setAttribute("register-error", true);
            redirectedPage = "/register-form.jsp";
        }
        response.sendRedirect(request.getContextPath() + redirectedPage);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}

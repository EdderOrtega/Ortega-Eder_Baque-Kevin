package dao.impl;

import dao.IDao;
import db.H2Connection;
import model.Odontologo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

public class DaoH2Odontologo implements IDao<Odontologo> {

    private static final Logger logger = Logger.getLogger(DaoH2Odontologo.class);
    private static final String INSERT = "INSERT INTO ODONTOLOGO VALUES (DEFAULT,?,?,?)";
    private static final String SELECT_TODOS = "SELECT * FROM ODONTOLOGO";


    @Override
    public Odontologo guardar(Odontologo odontologo) throws SQLException {
        Connection connection = null;
        Odontologo odontologoAgregado = null;

        try{
            connection = H2Connection.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement.setInt(1,odontologo.getNumeroMatricula());
            PreparedStatement.setString(2,odontologo.getNombre());
            PreparedStatement.setString(2,odontologo.getApellido());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                Integer idDesdeDd = resultSet.getInt(1);
                odontologoAgregado = new Odontologo(idDesdeDd, odontologo.getNumeroMatricula(), odontologo.getNombre(), odontologo.getApellido());
            }
            logger.info("Veterinario guardado en base de datos " + veterinario);
            connection.commit();

        }catch (Exception e) {
            logger.error("Error en la operación de guardar: " + e.getMessage(), e);
            e.printStackTrace();
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                logger.error("Error durante el rollback: " + ex.getMessage(), ex);
                throw new RuntimeException(ex);
            }
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error("Error al cerrar la conexión: " + e.getMessage(), e);
                e.printStackTrace();
            }
        }
        return odontologoAgregado;
    }

    @Override
    public List<Odontologo> buscarTodos() {
        Connection connection = null;
        List<Odontologo> odontologos = new ArrayList<>();

        try{
            connection = H2Connection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_TODOS);

            while (resultSet.next()) {
                Integer id = resultSet.getInt(1);
                int matriculaOdo = resultSet.getInt(2);
                String nombreOdo = resultSet.getString(3);
                String apellidoOdo = resultSet.getString(4);
                Odontologo veterinario = new Odontologo(id, matriculaOdo, nombreOdo, apellidoOdo);
                odontologos.add(veterinario);
                logger.info("Veterinario: " + veterinario);
            }
        }catch (Exception e) {
            logger.error("Error en la búsqueda de todos los veterinarios: " + e.getMessage(), e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error("Error al cerrar la conexión: " + e.getMessage(), e);
            }
        }
        return odontologos;
    }
}

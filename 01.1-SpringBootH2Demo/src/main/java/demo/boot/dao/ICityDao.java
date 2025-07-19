package demo.boot.dao;

import java.util.List;

import demo.boot.model.City;

public interface ICityDao {
    List<City> findAllCities();
    City findByCityId(Long id);
    void insertCity(City city);
    void deleteCity(int id);
    void updateCity(City city);
}

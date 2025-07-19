package demo.boot.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import demo.boot.dao.ICityDao;
import demo.boot.model.City;

@Service
public class CityService implements ICityService {

    @Autowired
    private ICityDao  cityDao;

    @Override
    public List<City> findAll() {

       return cityDao.findAllCities();
    }

    @Override
    public City findById(Long id) {

       return cityDao.findByCityId(id);
       
    }

	@Override
	public void insertCity(City city) {

cityDao.insertCity(city);
		
	}

	@Override
	public void deleteCity(int id) {
	 cityDao.deleteCity(id);
		
	}

	@Override
	public void updateCity(City city) {
		// TODO Auto-generated method stub
		cityDao.updateCity(city);
	}
}
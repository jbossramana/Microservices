package demo.boot.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import demo.boot.model.City;

@Repository   // this bean is proxied with throwsadvice
public class CityDao  implements ICityDao{


    @Autowired
    private JdbcTemplate jtm;

    @Override
    public List<City> findAllCities() {

        String sql = "SELECT * FROM cities";

        
        return jtm.query(sql, new BeanPropertyRowMapper<>(City.class));
    }
    
	@Override
	public List<City> findAllCitiesByPopulation(int people) {
		 String sql = "SELECT * FROM cities WHERE population >= ?"; 
		  return jtm.query(sql, new BeanPropertyRowMapper<>(City.class),new Object[]{people});
	}



    @Override
    public City findByCityId(Long id) {

        String sql = "SELECT * FROM cities WHERE id = ?";

        return jtm.queryForObject(sql, new BeanPropertyRowMapper<>(City.class),new Object[]{id});
    }

	@Override
	public void insertCity(City city) {
		
		int id = jtm.update("insert into cities (name,population) values(?,?)",city.getName(),city.getPopulation());
		
	}

	@Override
	public void deleteCity(int id) {
		// TODO Auto-generated method stub
		int val = jtm.update("delete from cities where id=?",id);
	
	}

	@Override
	public void updateCity(City city) {
	
		int val = jtm.update("update cities set population=? where id=?",city.getPopulation(), city.getId());
	}

}

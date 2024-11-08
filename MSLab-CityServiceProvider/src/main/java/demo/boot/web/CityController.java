package demo.boot.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.boot.model.City;
import demo.boot.service.ICityService;

import java.util.List;

@RestController
@RequestMapping("/city")
public class CityController {

    @Autowired
    private ICityService cityService;

    @GetMapping
    public List<City> findCities() {

        return cityService.findAll();
    }

    @GetMapping("/search")
    public List<City> findCitiesByPopulation(int people) {

        return cityService.findAllCitiesByPopulation(people);
    }
    
    @GetMapping("/{cityId}")
    public City findCity(@PathVariable Long cityId) {

        return cityService.findById(cityId);
    }

    @PostMapping
    public  void  insertCity(@RequestBody  City city)
    {
    	cityService.insertCity(city);
    }
    
    @DeleteMapping("/{cityId}")
    public void deleteCity(@PathVariable  int cityId)
    {
    	cityService.deleteCity(cityId);
    }
    
    @PutMapping("/{cityId}")
    void updateCity(@RequestBody City city)
    {
    
    	cityService.updateCity(city);
    }
    

}

package demo.boot.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import demo.boot.model.City;

@RestController
@RequestMapping("/client/city")
public class CityController {

	
	private final String BASE_URL="http://localhost:8081/city";
	
			
	@Autowired
	RestTemplate  restTemplate;
	
	
	@GetMapping
    public List<City> findCities()
    {
		return Arrays.asList(restTemplate.getForObject(BASE_URL, City[].class ));
    }
	
	
	 @GetMapping("/{cityId}")
	  public City findCity(@PathVariable Long cityId)
	  {
		 return restTemplate.getForObject(BASE_URL+"/"+cityId, City.class);
	  }
	 
	 
	 @PostMapping
	  public  void  insertCity(@RequestBody  City city)
	  {
		  restTemplate.postForObject(BASE_URL, city, Void.class);
	  }
	 
	
	 @PutMapping("/{cityId}")
	    void updateCity(@RequestBody City city, @PathVariable Long cityId)
	    {
		   restTemplate.put(BASE_URL+"/" + cityId , city);
	    }
	 
	 
	 @DeleteMapping("/{cityId}")
	    public void deleteCity(@PathVariable  int cityId)
	    {
		   restTemplate.delete(BASE_URL+"/"+cityId);
	    }
	 
	  @GetMapping("/search")
	    public List<City> findCitiesByPopulation(@RequestParam("people") int people) {
	       
	        City[] cities = restTemplate.getForObject(BASE_URL+"/search?people="+people, City[].class);

	        // Process and return the list of cities
	        return List.of(cities);
	    }
	  
}

package ch.mabaka.birdbox.website.pagecontroller;

import ch.mabaka.birdbox.website.persistence.dto.AggregatedMeasurementDTO;
import ch.mabaka.birdbox.website.persistence.entities.TemperatureMeassurementEntity;
import ch.mabaka.birdbox.website.persistence.repositories.TemperatureMeassurementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class StatisticsController {

    private final TemperatureMeassurementRepository temperatureMeassurementRepository;

    @Autowired
    public StatisticsController(TemperatureMeassurementRepository temperatureMeassurementRepository) {
        this.temperatureMeassurementRepository = temperatureMeassurementRepository;
    }

    @GetMapping("/statistics.html")
    public String statistics(Model model) {
        model.addAttribute("measurementsDay", temperatureMeassurementRepository.findLastDayMeasurements());
        model.addAttribute("measurementsWeek", temperatureMeassurementRepository.findLastWeekMeasurements()); // Use raw data for week
        model.addAttribute("measurementsMonth", temperatureMeassurementRepository.findLastMonthAggregated());
        model.addAttribute("measurementsQuarter", temperatureMeassurementRepository.findLastQuarterAggregated());
        return "statistics";
    }
}
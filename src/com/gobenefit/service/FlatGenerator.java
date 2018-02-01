package com.gobenefit.service;

import java.util.Date;
import java.util.concurrent.Callable;

import javax.persistence.PersistenceException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gobenefit.entity.impl.Flat;
import com.gobenefit.entity.impl.Tower;
import com.gobenefit.exception.FlatException;
import com.gobenefit.spring.BeanFactory;

public class FlatGenerator implements Callable<Flat> {
	private static final Logger LOGGER = LoggerFactory.getLogger(FlatGenerator.class);

	private Row row;

	private Tower tower;

	public FlatGenerator(Row row, Tower tower) {
		this.row = row;
		this.tower = tower;
	}

	@Override
	public Flat call() throws Exception {

		Flat flat = new Flat();
		Date date =new Date();
		flat.setTower(tower);
		flat.setCreationDate(date);
		flat.setApartment(tower.getApartment());
		Cell blockCell = row.getCell(0);
		flat.setPrefix(blockCell.getStringCellValue());
		Cell flatNoCell = row.getCell(1);
		if (flatNoCell!= null && flatNoCell.getNumericCellValue()<=0) {
			throw new FlatException("Flat number cannot be blank", -1, -1, flat.getPrefix());
		} else {
			flat.setFlatNumber((int) flatNoCell.getNumericCellValue());
		}
		Cell floorNoCell = row.getCell(2);
		if (floorNoCell == null) {
			throw new FlatException("Floor number cannot be blank", flat.getFlatNumber(), -1, flat.getPrefix());
		} else {
			flat.setFloorNumber((int) floorNoCell.getNumericCellValue());
		}
		FlatService flatService = (FlatService) BeanFactory.getWebContextBean("flatServiceImpl");
		try {
			flatService.createEntity(flat);
		} catch (PersistenceException e) {
			LOGGER.error(e.getMessage(), e);
			throw new FlatException(e.getMessage(), flat.getFlatNumber(), flat.getFloorNumber(), flat.getPrefix());
		}
		return flat;
	}
}

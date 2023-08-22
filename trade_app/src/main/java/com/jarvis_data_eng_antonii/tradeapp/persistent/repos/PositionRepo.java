package com.jarvis_data_eng_antonii.tradeapp.persistent.repos;

import com.jarvis_data_eng_antonii.tradeapp.persistent.entities.Position;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepo extends PagingAndSortingRepository<Position, String> {}
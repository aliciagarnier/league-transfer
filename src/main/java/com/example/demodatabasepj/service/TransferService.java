package com.example.demodatabasepj.service;

import com.example.demodatabasepj.dtos.TransferRecordDTO;
import com.example.demodatabasepj.models.*;



import com.example.demodatabasepj.repository.TransferRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;


@AllArgsConstructor

@Service
public class TransferService {


    private final TransferRepository transferRepository;

    public Transfer addTransfer (Player player, Club join, Club left, LocalDate date, BigDecimal fee) {

        // validations here.

        Transfer newTransfer = new Transfer(player, join, left, date, fee);
        return transferRepository.save(newTransfer);
    }

    public Transfer updateTransfer(TransferRecordDTO transferRecordDTO, Transfer transfer) {

        // validations here.
        // one transfer is equal to other one if the value, date, and player are the same.

        BeanUtils.copyProperties(transferRecordDTO, transfer);
        return transferRepository.save(transfer);
    }
    public List<Transfer> findAllTransfers() {
        return transferRepository.findAll();
    }

    public Optional<Transfer> findTransferById(UUID id) {
        if(Objects.isNull(id)) {
            throw new IllegalArgumentException("ID null when fetching for a transfer.");
        }
        return transferRepository.findById(id);
    }

    public void deleteTransfer(Transfer transfer) {
        transferRepository.delete(transfer);
    }

    public Page<Transfer> getAllTransfersWithFilter(String keyword, int pageNumber, String sortField, String sortDir)
    {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNumber - 1 , 20, sort);

        if(Objects.isNull(keyword)){
            return transferRepository.findAll(pageable);
        }

        return transferRepository.searchAllByPlayerNameOrJoinNameOrLeftName(keyword, pageable);
    }

    public Long countAllTransfersByPlayerNameOrJoinNameOrLeftName(String keyword){
        return transferRepository.countAllByPlayerNameOrJoinNameOrLeftName(keyword);
    }


}

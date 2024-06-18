package service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.hibernate.mapping.List;
import org.springframework.stereotype.Service;

import model.Transfer;
import repository.TransferRepository;

@Service
public class TransferService {

    private final TransferRepository transferRepository;

    public TransferService(TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    public Transfer scheduleTransfer(Transfer transfer) {
        long daysBetween = ChronoUnit.DAYS.between(LocalDate.now(), transfer.getTransferDate());
        double fee = calculateFee(daysBetween, ((Transfer) transfer).getAmount());
        transfer.setFee(fee);
        transfer.setScheduleDate(LocalDate.now());
        return transferRepository.save(transfer);
    }

    private double calculateFee(long daysBetween, double amount) {
        if (daysBetween == 0) {
            return 3.00 + (amount * 0.025);
        } else if (daysBetween >= 1 && daysBetween <= 10) {
            return 12.00;
        } else if (daysBetween >= 11 && daysBetween <= 20) {
            return amount * 0.082;
        } else if (daysBetween >= 21 && daysBetween <= 30) {
            return amount * 0.069;
        } else if (daysBetween >= 31 && daysBetween <= 40) {
            return amount * 0.047;
        } else if (daysBetween >= 41 && daysBetween <= 50) {
            return amount * 0.017;
        } else {
            throw new IllegalArgumentException("A data da transferência ultrapassa o limite de 50 dias");
        }
    }

    public java.util.List<Transfer> getAllTransfers() {
        return transferRepository.findAll();
    }
}
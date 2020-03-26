package org.openlmis.vaccine.service.warehouse;

import org.openlmis.vaccine.domain.wms.Transfer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransferService {

    @Transactional
    public Transfer save(Transfer item, Long loggedInUserId) {
        return item;
    }

    public void update(Transfer item, Long loggedInUserId) {

    }

    public List<Transfer> search(String searchParam) {
        return null;
    }

    public List<Transfer> getAll() {
        return null;
    }
}

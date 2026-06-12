package dao;

import java.util.List;
import model.AturanGaji;

public interface IAturanGajiDAO {
    void insertAturan(AturanGaji aturan);
    List<AturanGaji> getAllAturan();
    void updateAturan(AturanGaji aturan);
    void deleteAturan(int aturanId);

    /** Mengambil satu aturan gaji (KPI) berdasarkan id; null bila tidak ada. */
    AturanGaji getAturanById(int aturanId);
}

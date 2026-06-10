package dao;

import connection.DatabaseConnection;
import model.SlipGaji;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SlipGajiDAOImpl implements ISlipGajiDAO {

    @Override
    public void insertDraftGaji(SlipGaji slip) {
        Connection conn = DatabaseConnection.getInstance();
        String query = "INSERT INTO slip_gaji (user_id, aturan_id, periode_bulan, periode_tahun, " +
                       "nominal_gaji_pokok, total_bonus, total_potongan, kustomisasi_gaji, gaji_bersih, status_approval) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, 0, ?, 'Draft_HR')";
                       
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, slip.getUserId());
            ps.setInt(2, slip.getAturanId());
            ps.setInt(3, slip.getPeriodeBulan());
            ps.setInt(4, slip.getPeriodeTahun());
            ps.setDouble(5, slip.getNominalGajiPokok());
            ps.setDouble(6, slip.getTotalBonus());
            ps.setDouble(7, slip.getTotalPotongan());
            double bersihAwal = slip.getNominalGajiPokok() + slip.getTotalBonus() - slip.getTotalPotongan();
            ps.setDouble(8, bersihAwal);
            ps.executeUpdate();
            System.out.println("Draft gaji berhasil dibuat.");
        } catch (SQLException e) {
            System.out.println("Error insert draft gaji: " + e.getMessage());
        }
    }

    @Override
    public void updateKustomisasiOwner(int idSlip, double nominalKustom) {
        Connection conn = DatabaseConnection.getInstance();
        String query = "UPDATE slip_gaji SET kustomisasi_gaji = ?, " +
                       "gaji_bersih = (nominal_gaji_pokok + total_bonus - total_potongan + ?) " +
                       "WHERE id_slip = ?";
                       
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setDouble(1, nominalKustom);
            ps.setDouble(2, nominalKustom);
            ps.setInt(3, idSlip);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error update kustomisasi: " + e.getMessage());
        }
    }

    @Override
    public void approveGajiFinal(int idSlip) {
        Connection conn = DatabaseConnection.getInstance();
        String query = "UPDATE slip_gaji SET status_approval = 'Approved_Owner' WHERE id_slip = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, idSlip);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error approval gaji: " + e.getMessage());
        }
    }

    @Override
    public List<SlipGaji> getAllSlipGaji() {
        return ambilDataSlipGaji("SELECT * FROM slip_gaji ORDER BY periode_tahun DESC, periode_bulan DESC");
    }

    @Override
    public List<SlipGaji> getSlipGajiByBulan(int bulan, int tahun) {
        String query = "SELECT * FROM slip_gaji WHERE periode_bulan = " + bulan + " AND periode_tahun = " + tahun;
        return ambilDataSlipGaji(query);
    }
    
    private List<SlipGaji> ambilDataSlipGaji(String query) {
        List<SlipGaji> list = new ArrayList<>();
        Connection conn = DatabaseConnection.getInstance();
        
        try (PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
             
            while (rs.next()) {
                SlipGaji s = new SlipGaji();
                s.setIdSlip(rs.getInt("id_slip"));
                s.setUserId(rs.getInt("user_id"));
                s.setAturanId(rs.getInt("aturan_id"));
                s.setPeriodeBulan(rs.getInt("periode_bulan"));
                s.setPeriodeTahun(rs.getInt("periode_tahun"));
                s.setNominalGajiPokok(rs.getDouble("nominal_gaji_pokok"));
                s.setTotalBonus(rs.getDouble("total_bonus"));
                s.setTotalPotongan(rs.getDouble("total_potongan"));
                s.setKustomisasiGaji(rs.getDouble("kustomisasi_gaji"));
                s.setGajiBersih(rs.getDouble("gaji_bersih"));
                s.setStatusApproval(rs.getString("status_approval"));
                list.add(s);
            }
        } catch (SQLException e) {
            System.out.println("Error get slip gaji: " + e.getMessage());
        }
        return list;
    }
}
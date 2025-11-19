package pertemuan14;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import pertemuan14.MataKuliah;
import pertemuan14.Dosen;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Faiq
 */
@Entity
@Table(name = "mengajar")
@NamedQueries({
    @NamedQuery(name = "Mengajar.findAll", query = "SELECT m FROM Mengajar m ORDER BY m.kodeMengajar"),
    @NamedQuery(name = "Mengajar.findByKodeMengajar", query = "SELECT m FROM Mengajar m WHERE m.kodeMengajar = :kodeMengajar"),
    @NamedQuery(name = "Mengajar.findByTahunAjaran", query = "SELECT m FROM Mengajar m WHERE m.tahunAjaran = :tahunAjaran")})
public class Mengajar implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "kode_mengajar")
    private String kodeMengajar;
    @Column(name = "tahun_ajaran")
    private String tahunAjaran;
    @JoinColumn(name = "id_dosen", referencedColumnName = "id_dosen")
    @ManyToOne(optional = false)
    private Dosen idDosen;
    @JoinColumn(name = "kode_matkul", referencedColumnName = "kode_matkul")
    @ManyToOne(optional = false)
    private MataKuliah kodeMatkul;

    public Mengajar() {
    }

    public Mengajar(String kodeMengajar) {
        this.kodeMengajar = kodeMengajar;
    }

    public String getKodeMengajar() {
        return kodeMengajar;
    }

    public void setKodeMengajar(String kodeMengajar) {
        this.kodeMengajar = kodeMengajar;
    }

    public String getTahunAjaran() {
        return tahunAjaran;
    }

    public void setTahunAjaran(String tahunAjaran) {
        this.tahunAjaran = tahunAjaran;
    }

    public Dosen getIdDosen() {
        return idDosen;
    }

    public void setIdDosen(Dosen idDosen) {
        this.idDosen = idDosen;
    }

    public MataKuliah getKodeMatkul() {
        return kodeMatkul;
    }

    public void setKodeMatkul(MataKuliah kodeMatkul) {
        this.kodeMatkul = kodeMatkul;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (kodeMengajar != null ? kodeMengajar.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Mengajar)) {
            return false;
        }
        Mengajar other = (Mengajar) object;
        if ((this.kodeMengajar == null && other.kodeMengajar != null) || (this.kodeMengajar != null && !this.kodeMengajar.equals(other.kodeMengajar))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pertemuan13.Mengajar[ kodeMengajar=" + kodeMengajar + " ]";
    }
    
}

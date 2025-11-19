/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pertemuan14;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Faiq
 */
@Entity
@Table(name = "dosen")
@NamedQueries({
    @NamedQuery(name = "Dosen.findAll", query = "SELECT d FROM Dosen d ORDER BY d.idDosen"),
    @NamedQuery(name = "Dosen.findByIdDosen", query = "SELECT d FROM Dosen d WHERE d.idDosen = :idDosen"),
    @NamedQuery(name = "Dosen.findByNamaDosen", query = "SELECT d FROM Dosen d WHERE d.namaDosen = :namaDosen"),
    @NamedQuery(name = "Dosen.findByNip", query = "SELECT d FROM Dosen d WHERE d.nip = :nip"),
    @NamedQuery(name = "Dosen.findByEmail", query = "SELECT d FROM Dosen d WHERE d.email = :email")})
public class Dosen implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_dosen")
    private String idDosen;
    @Basic(optional = false)
    @Column(name = "nama_dosen")
    private String namaDosen;
    @Column(name = "nip")
    private String nip;
    @Column(name = "email")
    private String email;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idDosen")
    private Collection<Mengajar> mengajarCollection;

    public Dosen() {
    }

    public Dosen(String idDosen) {
        this.idDosen = idDosen;
    }

    public Dosen(String idDosen, String namaDosen) {
        this.idDosen = idDosen;
        this.namaDosen = namaDosen;
    }

    public String getIdDosen() {
        return idDosen;
    }

    public void setIdDosen(String idDosen) {
        this.idDosen = idDosen;
    }

    public String getNamaDosen() {
        return namaDosen;
    }

    public void setNamaDosen(String namaDosen) {
        this.namaDosen = namaDosen;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Collection<Mengajar> getMengajarCollection() {
        return mengajarCollection;
    }

    public void setMengajarCollection(Collection<Mengajar> mengajarCollection) {
        this.mengajarCollection = mengajarCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDosen != null ? idDosen.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Dosen)) {
            return false;
        }
        Dosen other = (Dosen) object;
        if ((this.idDosen == null && other.idDosen != null) || (this.idDosen != null && !this.idDosen.equals(other.idDosen))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return namaDosen;
    }
    
}

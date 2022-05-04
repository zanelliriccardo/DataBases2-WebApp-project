package it.polimi.gma.entities;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Set;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Product
 *
 */
@NamedQueries({
	@NamedQuery(name="Product.findAll",
				query="SELECT p FROM Product p"),
	@NamedQuery(name="Product.findByName",
				query="SELECT p "
					+ "FROM Product p "
					+ "WHERE p.name = :nameValue"),
})

@Entity
@Table(name="PRODUCT", schema = "gmadb")
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;

	// ====================
	// Attributes
	// ====================
	
	@Column(nullable = false)
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable = false, length = 255)
	private String name;
	
	@Lob
	@Basic(fetch = FetchType.LAZY) // bisogna vedere se si può mettere eager dato che praticamente ogni volta che prendi il questionario prendi anche il prodotto e quindi l'immagine da mostrare
	@Column(name = "IMAGE") // potrebbe esserci un prodotto senza immagine
	private byte[] image;
	
	// ====================
	// Relations
	// ====================
	
	@OneToMany(mappedBy="product") // Lasciamo di default (LAZY) perchè ci sono dei casi in cui prendi il prodotto senza le review
	private Set<Review> reviews;
	
	@OneToMany(mappedBy="product") // Lasciamo di default perchè se prendiamo un questionario non ci interessano gli altri questionari relativi allo stesso product
	private Set<Questionnaire> questinonaires;
	
	// ====================
	// Constructors
	// ====================
	
	public Product() {
		super();
	}
	
	public Product(String productName, byte[] productImage){
        this.name = productName;
        this.image = productImage;
    }
	
	// ====================
	// Getters & Setters
	// ====================
	   
	public Set<Questionnaire> getQuestinonaires() {
		return questinonaires;
	}

	public void setQuestinonaires(Set<Questionnaire> questinonaires) {
		this.questinonaires = questinonaires;
	}

	public long getIdProduct() {	return this.id;}
    public void setIdProduct(long idProduct) {	this.id = idProduct;  }
    
    public String getName() {	return name;	}
	public void setName(String name) {	this.name = name;    }
    
    public byte[] getProductImage() {	return image;  }
    public void setProductImage(byte[] productImage) {	this.image = productImage;	}

    public Set<Review> getReviews() {
		return reviews;
	}

	public void setReviews(Set<Review> reviews) {
		this.reviews = reviews;
	}
    // ====================
  	// Hashcode & Equals
  	// ====================
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (id != product.id) return false;
        if (name != null ? !name.equals(product.name) : product.name != null) return false;
        if (!Arrays.equals(image, product.image)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(image);
        return result;
    }
}

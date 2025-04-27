package entities;

import java.sql.Date;

public class Category {
        private int id;
        private String nom;
        private String desc;


        public Category(int id, String nom, String description) {
        }



        public Category(String nom, String desc) {
            this.nom = nom;
            this.desc = desc;

        }

    public Category() {

    }


    public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNom() {
            return nom;
        }

        public void setNom(String nom) {
            this.nom = nom;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        @Override
        public String toString() {
            return "Product{" +
                    "id=" + id +
                    ", nom='" + nom + '\'' +
                    ", desc='" + desc + '\'' +

                    '}';
        }


}

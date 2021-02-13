package com.telegrambot.realdebrid.services.dtos;


import javax.persistence.*;

@Entity
@Table(name = "user")
public class UserDTO {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "telegram_id")
    private String telegramId;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "authentication_id")
    private Authentication authenticationDTO;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "token_id")
    private Token token;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTelegramId() {
        return telegramId;
    }

    public void setTelegramId(String telegramId) {
        this.telegramId = telegramId;
    }

    public Authentication getAuthenticationDTO() {
        return authenticationDTO;
    }

    public void setAuthenticationDTO(Authentication authenticationDTO) {
        this.authenticationDTO = authenticationDTO;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", telegramId='" + telegramId + '\'' +
                ", authenticationDTO=" + authenticationDTO +
                ", clientDTO=" + client +
                ", tokenDTO=" + token +
                '}';
    }


    public static final class UserDTOBuilder {
        private int id;
        private String userName;
        private String telegramId;
        private Authentication authenticationDTO;
        private Client client;
        private Token token;

        private UserDTOBuilder() {
        }

        public static UserDTOBuilder anUserDTO() {
            return new UserDTOBuilder();
        }

        public UserDTOBuilder withId(int id) {
            this.id = id;
            return this;
        }

        public UserDTOBuilder withUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public UserDTOBuilder withTelegramId(String telegramId) {
            this.telegramId = telegramId;
            return this;
        }

        public UserDTOBuilder withAuthenticationDTO(Authentication authenticationDTO) {
            this.authenticationDTO = authenticationDTO;
            return this;
        }

        public UserDTOBuilder withClientDTO(Client client) {
            this.client = client;
            return this;
        }

        public UserDTOBuilder withTokenDTO(Token token) {
            this.token = token;
            return this;
        }

        public UserDTO build() {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(id);
            userDTO.setUserName(userName);
            userDTO.setTelegramId(telegramId);
            userDTO.setAuthenticationDTO(authenticationDTO);
            userDTO.setClient(client);
            userDTO.setToken(token);
            return userDTO;
        }
    }
}

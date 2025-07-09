package br.com.alura.literalura_api;

import br.com.alura.literalura_api.view.MenuView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApiApplication implements CommandLineRunner {

	@Autowired
	private MenuView menuView;

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		menuView.showMenu();
	}
}

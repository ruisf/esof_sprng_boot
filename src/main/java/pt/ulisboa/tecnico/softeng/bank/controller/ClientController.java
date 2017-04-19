package pt.ulisboa.tecnico.softeng.bank.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.bank.domain.Client;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

@Controller
@RequestMapping(value = "/bank/{code}/clients")
public class ClientController {
	private static Logger logger = LoggerFactory.getLogger(ClientController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String clientForm(Model model) {
		logger.info("clientForm");
		
		// Tenho dúvidas /////
		Bank bank=new Bank();
		//////////////////////
		
		model.addAttribute("bank", bank);
		model.addAttribute("client", new Client());
		model.addAttribute("clients", bank.getClients());
		return "clients";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String clientSubmit(Model model, @ModelAttribute Client client) {
		logger.info("clientSubmit bank:{}, id:{}, name:{}, age:{}", client.getBank(), client.getId(), client.getName(), client.getAge());

		try {
			new Client(client.getBank(), client.getId(), client.getName(), client.getAge());
		} catch (BankException be) {
			model.addAttribute("error", "Error: it was not possible to create the client");
			model.addAttribute("client", client);
			model.addAttribute("clients", client.getBank().getClients());
			return "clients";
		}

		return "redirect:/clients";
	}

	@RequestMapping(value = "/bank/{code}/clients/{id}/client", method = RequestMethod.GET)
	public String showClient(Model model, @PathVariable String code, @PathVariable String id) {
		logger.info("showClient code:{},id:{}", code, id);

		Bank bank = Bank.getBankByCode(code);

		new Client(bank, "ID01", "Zé", 22);
		new Client(bank, "ID02", "Manel", 44);

		model.addAttribute("bank", bank);
		return "bank";
	}
}
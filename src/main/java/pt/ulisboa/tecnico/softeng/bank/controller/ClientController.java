package pt.ulisboa.tecnico.softeng.bank.controller;

import java.util.HashSet;
import java.util.Set;

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
@RequestMapping(value = "/banks/bank/{code}/client")
public class ClientController {
	private static Logger logger = LoggerFactory.getLogger(ClientController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String clientForm(Model model, @ModelAttribute Bank bank) {//preciso cliente tb?
		logger.info("clientForm bank:{}", bank);
		
		Client client = new Client();
		client.setBank(bank);
		
		model.addAttribute("bank", bank);
		model.addAttribute("client", client);
		model.addAttribute("clients", bank.getClients());
		return "redirect:/banks/bank/"+bank.getCode();
	}

	@RequestMapping(method = RequestMethod.POST)
	public String clientSubmit(Model model, @ModelAttribute Client client, @ModelAttribute Bank bank) {
		logger.info("clientSubmit bank:{}, id:{}, name:{}, age:{}", bank, client.getId(), client.getName(), client.getAge());

		try {
			new Client(bank, client.getId(), client.getName(), client.getAge());
			//model.addAttribute("bank", client.getBank());
			client.setBank(bank);
			model.addAttribute("client", client);
			model.addAttribute("clients", client.getBank().getClients().add(client));
		} catch (BankException be) {
			model.addAttribute("error", "Error: it was not possible to create the client");
			
			model.addAttribute("bank", client.getBank());
			model.addAttribute("client", client);
			model.addAttribute("clients", client.getBank().getClients());
			return "redirect:/banks/bank/"+bank.getCode();
		}

		return "redirect:/banks/bank/"+bank.getCode();
	}
/*
	@RequestMapping(value = "/banks/bank/{code}/client", method = RequestMethod.GET)
	public String showClient(Model model, @PathVariable String code) {
		logger.info("showClient code:{},id:{}", code);

		Bank bank = Bank.getBankByCode(code);
		logger.info("showBank bank.code:{}", bank.getCode());
		Set<Client> listClient = bank.getClients();
		model.addAttribute("bank", bank);
		model.addAttribute("clients", listClient);
		return "redirect:/banks/bank/"+bank.getCode();
	}
	*/
}
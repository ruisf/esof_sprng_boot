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
@RequestMapping(value = "/banks/bank/{code}/client")
public class ClientController {
	private static Logger logger = LoggerFactory.getLogger(ClientController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String clientForm(Model model, @ModelAttribute Bank bank) {
		logger.info("clientForm bank:{}", bank);
		
		// Tenho dúvidas /////
		//Bank bank=new Bank();
		//////////////////////
		Client client = new Client();
		client.setBank(bank);
		
		model.addAttribute("bank", bank);
		model.addAttribute("client", client);
		model.addAttribute("clients", bank.getClients());
		return "bank";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String clientSubmit(Model model, @ModelAttribute Client client) {
		logger.info("clientSubmit bank:{}, id:{}, name:{}, age:{}", client.getBank(), client.getId(), client.getName(), client.getAge());

		try {
			new Client(client.getBank(), client.getId(), client.getName(), client.getAge());
			model.addAttribute("bank", client.getBank());
			model.addAttribute("client", client);
			model.addAttribute("clients", client.getBank().getClients());
		} catch (BankException be) {
			model.addAttribute("error", "Error: it was not possible to create the client");
			
			model.addAttribute("bank", client.getBank());
			model.addAttribute("client", client);
			model.addAttribute("clients", client.getBank().getClients());
			return "bank";
		}

		return "redirect:/bank/"+client.getBank().getCode()+"/client";
	}

	@RequestMapping(value = "/{id}/client", method = RequestMethod.GET)
	public String showClient(Model model, @PathVariable String code, @PathVariable String id) {
		logger.info("showClient code:{},id:{}", code, id);

		Bank bank = Bank.getBankByCode(code);
		Client client = null;
		for(Client cli : bank.getClients()){
			if(cli.getId()==id)
				client=cli;
		}
		model.addAttribute("bank", bank);
		
		model.addAttribute("error", "Error: it was not possible to view the client");
		model.addAttribute("client", client);
		model.addAttribute("clients", client.getBank().getClients());
		return "client"; //página para onde volta
	}
}
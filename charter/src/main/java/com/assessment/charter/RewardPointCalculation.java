package com.assessment.charter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RewardPointCalculation {

	@SuppressWarnings("unchecked")
	@PostMapping("/")
	@CrossOrigin(origins = "*")
	public Object develop(@RequestBody Object request) {
		
		HashMap<String,List<Map<String,Object>>> req = (HashMap<String,List<Map<String,Object>>>) request;
		
		Map<String, Map<String, List<Long>>> transactionOfUsers = new HashMap<>();
		
		for(Entry<String,List<Map<String,Object>>> ele : req.entrySet()) {
			String userName = ele.getKey();
			List<Map<String,Object>> monthsMap = ele.getValue();
			//HashMap<String, ArrayList<Long>> monthsMap = ele.getValue();
			
			Map<String, List<Long>> map_user = new HashMap<>();
			
			for(Map<String,Object> transaction : monthsMap) {
				String month = transaction.get("month").toString();
				List<Integer> amounts = (List<Integer>)transaction.get("transaction");
				List<Long> transaction_month_user = amounts.stream().map(amt -> Long.valueOf(amt)).collect(Collectors.toList());
				map_user.put(month, transaction_month_user);
			}
			transactionOfUsers.put(userName, map_user);
		}
		// method call for each user's total reward point calculation for all given
		// three months
		Map<String, Long> result = execute(transactionOfUsers);

		return result;
	}

	// method to get all user's total reward point calculation for all given three
	// months
	private static Map<String, Long> execute(Map<String, Map<String, List<Long>>> transactionOfUsers) {
		final Map<String, Long> map = new HashMap<>();
		transactionOfUsers.forEach((user, transactionsOfMonths) -> {
			map.put(user, calculateRewardForEachUser(transactionsOfMonths));
		});
		return map;
	}

	// Three months reward point addition for each user
	private static long calculateRewardForEachUser(Map<String, List<Long>> transactionsOfMonths) {
		long totalRewardForAllMonths = transactionsOfMonths.entrySet().stream().mapToLong((e) -> {
			return calculationTotalRewardPoint(e.getValue());
		}).sum();
		return totalRewardForAllMonths;
	}

	// reward point calculation for each transaction
	private static long calculateRewardPoints(final long transactionAmt) {
		if (transactionAmt >= 50 && transactionAmt <= 100) {
			return transactionAmt - 50;
		} else if (transactionAmt > 100) {
			return (transactionAmt - 100) * 2 + 50;
		} else {
			return 0;
		}
	}

	// Total reward point addition of each month's transactions
	private static long calculationTotalRewardPoint(final List<Long> collection_rewardPoints_eachTransaction) {
		return collection_rewardPoints_eachTransaction.stream().mapToLong((transactionAmt) -> {
			return calculateRewardPoints(transactionAmt);
		}).sum();
	}
}

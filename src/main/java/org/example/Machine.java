package org.example;

import org.example.exceptions.InsufficientFundsException;
import org.example.exceptions.NoAvailableCoinsException;
import org.example.exceptions.OutOfStockException;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class Machine {
    private final TreeMap<Product, Integer> products ; // <Product, Quantity>
    private final TreeMap<Coin, Integer> coins;

    public Machine(){
        this.products = new TreeMap<>();
        this.coins = new TreeMap<>(Collections.reverseOrder());
    }

    public void addProduct(Product productName, Integer quantity){
        int productQuantity = this.products.getOrDefault(productName, 0);
        this.products.put(productName, productQuantity+quantity);
    }
    public void addCoins(Coin coin, Integer amountOfCoin){
        this.coins.put(coin, coins.getOrDefault(coin,0)+amountOfCoin);
    }

    public TreeMap<Coin, Integer> getCoins() {
        return coins;
    }

    public TreeMap<Product, Integer> getProducts() {
        return products;
    }

    public void buyProduct(Product productName, TreeMap<Coin, Integer> moneyInserted){
        int neededMoney = productName.getPrice();
        int money = getSumOfMoneyInserted(moneyInserted);

        if(!isProductExist(productName))
            throw new OutOfStockException("This product doesn't exist any more");
        if(money < neededMoney)
            throw new InsufficientFundsException("Your money not enough to buy this product");

        int remainingMoney = money - neededMoney;
        for(Map.Entry<Coin,Integer> entry : coins.entrySet()){
            int neededCoins = remainingMoney / entry.getKey().getValue();
            int tackingCoins = Math.min(entry.getValue(), neededCoins);
            this.coins.put(entry.getKey(), entry.getValue()-tackingCoins);
            remainingMoney -= tackingCoins* entry.getKey().getValue();
            if(remainingMoney == 0) {
                decreaseProductQuantity(productName);
                addToAvailableFunds(moneyInserted);
                break;
            }
        }
        if(remainingMoney != 0)
            throw new NoAvailableCoinsException("No available coins for remaining money");
    }

    private int getSumOfMoneyInserted(TreeMap<Coin, Integer> money){
        int sum = 0;
        for(Map.Entry<Coin, Integer> entry : money.entrySet()){
            sum += entry.getKey().getValue() * entry.getValue();
        }
        return sum;
    }

    private void addToAvailableFunds(TreeMap<Coin, Integer> money){
        for(Map.Entry<Coin,Integer> entry : money.entrySet()){
            coins.put(entry.getKey(), coins.get(entry.getKey()) + entry.getValue());
        }
    }

    public int getProductQuantity(Product productName) {
        return products.getOrDefault(productName, 0);
    }

    private boolean isProductExist(Product productName){
        // product exist and his quantity greater than 0
        return this.products.containsKey(productName) && this.products.get(productName) > 0;
    }
    private void decreaseProductQuantity(Product productName){
        int currentQuantity = products.getOrDefault(productName, 0);
        this.products.put(productName, currentQuantity-1);

    }

}

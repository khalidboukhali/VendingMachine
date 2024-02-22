import org.example.Coin;
import org.example.Machine;
import org.example.Product;
import org.example.exceptions.InsufficientFundsException;
import org.example.exceptions.NoAvailableCoinsException;
import org.example.exceptions.OutOfStockException;
import org.junit.jupiter.api.Test;

import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

public class MachineTest {
    @Test
    public void testAddProduct() {
        Machine machine = new Machine();

        // Add a product
        machine.addProduct(Product.COCA,  5);

        // Retrieve product Quantity
        int cocaQuantity = machine.getProductQuantity(Product.COCA);

        assertEquals(5, cocaQuantity);    // Check quantity
    }

    @Test
    public void testAddCoins() {
        Machine machine = new Machine();

        machine.addCoins(Coin.FIVE, 10);
        machine.addCoins(Coin.TEN, 5);

        assertEquals(10, machine.getCoins().get(Coin.FIVE));
        assertEquals(5, machine.getCoins().get(Coin.TEN));

        // Add more coins of the same denomination
        machine.addCoins(Coin.FIVE, 8);

        assertEquals(18, machine.getCoins().get(Coin.FIVE));
    }

    @Test
    public void testBuyProductWithEmptyStock(){
        Machine machine = new Machine();
        machine.addProduct(Product.COCA, 0);

        TreeMap<Coin, Integer> insertedMoney = new TreeMap<>();
        insertedMoney.put(Coin.TEN,1);

        assertThrows(OutOfStockException.class, () -> machine.buyProduct(Product.COCA, insertedMoney) );
    }

    @Test
    public void testBuyWithNoAvailableCoins(){
        Machine machine = new Machine();

        machine.addCoins(Coin.TWO, 3);
        machine.addCoins(Coin.FIVE, 10);
        machine.addCoins(Coin.TEN, 5);

        machine.addProduct(Product.COCA, 5);

        TreeMap<Coin, Integer> insertedMoney = new TreeMap<>();
        insertedMoney.put(Coin.TEN,1);
        insertedMoney.put(Coin.FIVE,1);
        insertedMoney.put(Coin.TWO,1);
        insertedMoney.put(Coin.ONE,1);

        assertThrows(NoAvailableCoinsException.class, () -> machine.buyProduct(Product.COCA, insertedMoney) );
    }

    @Test
    public void testBuyWithAvailableProductAndCoins(){
        Machine machine = new Machine();

        machine.addCoins(Coin.ONE, 4);
        machine.addCoins(Coin.TWO, 3);
        machine.addCoins(Coin.FIVE, 10);
        machine.addCoins(Coin.TEN, 5);

        machine.addProduct(Product.COCA, 5);

        TreeMap<Coin, Integer> insertedMoney = new TreeMap<>();
        insertedMoney.put(Coin.TEN,1);
        insertedMoney.put(Coin.TWO,2);
        insertedMoney.put(Coin.ONE,1);

        machine.buyProduct(Product.COCA, insertedMoney);

        assertEquals(4, machine.getCoins().get(Coin.ONE));
        assertEquals(4, machine.getCoins().get(Coin.TWO));
        assertEquals(9, machine.getCoins().get(Coin.FIVE));
        assertEquals(6, machine.getCoins().get(Coin.TEN)); // increased because user insert one 10
    }

    @Test
    public void testBuyWithoutEnoughMoney(){
        Machine machine = new Machine();

        machine.addCoins(Coin.TWO, 3);
        machine.addCoins(Coin.FIVE, 10);
        machine.addCoins(Coin.TEN, 5);

        machine.addProduct(Product.COCA, 5);

        TreeMap<Coin, Integer> insertedMoney = new TreeMap<>();
        insertedMoney.put(Coin.TWO,2);
        insertedMoney.put(Coin.ONE,1);

        assertThrows(InsufficientFundsException.class, () -> machine.buyProduct(Product.COCA, insertedMoney) );
    }

    @Test
    public void testDecreasingProductQuantityAfterBuying(){
        Machine machine = new Machine();

        machine.addCoins(Coin.TWO, 3);
        machine.addCoins(Coin.FIVE, 10);
        machine.addCoins(Coin.TEN, 5);

        machine.addProduct(Product.COCA, 5);

        TreeMap<Coin, Integer> insertedMoney = new TreeMap<>();
        insertedMoney.put(Coin.TEN,1);
        insertedMoney.put(Coin.TWO,2);

        machine.buyProduct(Product.COCA, insertedMoney);

        assertEquals(4, machine.getProducts().get(Product.COCA));
    }
}

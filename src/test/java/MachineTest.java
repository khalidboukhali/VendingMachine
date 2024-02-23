import org.example.Coin;
import org.example.Machine;
import org.example.Product;
import org.example.exceptions.InsufficientFundsException;
import org.example.exceptions.NoAvailableCoinsException;
import org.example.exceptions.OutOfStockException;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class MachineTest {
    @Test
    public void testAddProduct() {
        Machine machine = new Machine();

        // Add a product
        machine.increaseProductQuantity(Product.COCA,  5);

        // Retrieve product Quantity
        int cocaQuantity = machine.getProductQuantity(Product.COCA);

        assertEquals(5, cocaQuantity);    // Check quantity
    }

    @Test
    public void testAddCoins() {
        Machine machine = new Machine();

        machine.increaseCoinsAmount(Coin.FIVE, 10);
        machine.increaseCoinsAmount(Coin.TEN, 5);

        assertEquals(10, machine.getCoins().get(Coin.FIVE));
        assertEquals(5, machine.getCoins().get(Coin.TEN));

        // Add more coins of the same denomination
        machine.increaseCoinsAmount(Coin.FIVE, 8);

        assertEquals(18, machine.getCoins().get(Coin.FIVE));
    }

    @Test
    public void testBuyProductWithEmptyStock(){
        Machine machine = new Machine();
        machine.increaseProductQuantity(Product.COCA, 0);

        machine.insertMoney(Coin.TEN);

        assertThrows(OutOfStockException.class, () -> machine.buyProduct(Product.COCA) );
    }

    @Test
    public void testBuyWithNoAvailableCoins(){
        Machine machine = new Machine();

        machine.increaseCoinsAmount(Coin.TWO, 3);
        machine.increaseCoinsAmount(Coin.FIVE, 10);
        machine.increaseCoinsAmount(Coin.TEN, 5);

        machine.increaseProductQuantity(Product.COCA, 5);

        machine.insertMoney(Coin.TEN);
        machine.insertMoney(Coin.FIVE);
        machine.insertMoney(Coin.TWO);
        machine.insertMoney(Coin.ONE);

        assertThrows(NoAvailableCoinsException.class, () -> machine.buyProduct(Product.COCA));
    }

    @Test
    public void testBuyWithAvailableProductAndCoins(){
        Machine machine = new Machine();

        machine.increaseCoinsAmount(Coin.TWO, 4);
        machine.increaseCoinsAmount(Coin.FIVE, 10);
        machine.increaseCoinsAmount(Coin.TEN, 5);

        machine.increaseProductQuantity(Product.BUENO, 5);

        machine.insertMoney(Coin.TEN);
        machine.insertMoney(Coin.TEN);

        machine.buyProduct(Product.BUENO);

        assertEquals(0, machine.getCoins().get(Coin.TWO));
        assertEquals(10, machine.getCoins().get(Coin.FIVE));
        assertEquals(7, machine.getCoins().get(Coin.TEN)); // increased because user insert one 10
    }

    @Test
    public void testBuyWithoutEnoughMoney(){
        Machine machine = new Machine();

        machine.increaseProductQuantity(Product.COCA, 5);

        machine.insertMoney(Coin.TWO);
        machine.insertMoney(Coin.TWO);
        machine.insertMoney(Coin.ONE);

        assertThrows(InsufficientFundsException.class, () -> machine.buyProduct(Product.COCA) );
    }

    @Test
    public void testDecreasingProductQuantityAfterBuying(){
        Machine machine = new Machine();

        machine.increaseCoinsAmount(Coin.TWO, 3);
        machine.increaseCoinsAmount(Coin.FIVE, 10);
        machine.increaseCoinsAmount(Coin.TEN, 5);

        machine.increaseProductQuantity(Product.COCA, 5);

        machine.insertMoney(Coin.TEN);
        machine.insertMoney(Coin.TWO);
        machine.insertMoney(Coin.TWO);

        machine.buyProduct(Product.COCA);

        assertEquals(4, machine.getProducts().get(Product.COCA));
    }
}

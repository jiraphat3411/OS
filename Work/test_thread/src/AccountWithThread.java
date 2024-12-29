/*import java.util.concurrent.*;
public class AccountWithThread {
    private static Account account = new Account();
    public static void main(String[] args){
        ExecutorService executor = Executors.newCachedThreadPool();
        for(int i=0;i<100;i++){
            executor.execute(new AddAPennyTask());
        }
        executor.shutdown();
        while(!executor.isTerminated()){
        }
        System.out.println("What is valance?"+accout.getBalance());
    }
}

private static class AddAPennyTask extends Thread{
    public void run(){
        account.deposit(1);
    }
}
private static class Account{
    private int balance = 0;
    public int getBalance(){
        return balance;
    }
}
*/
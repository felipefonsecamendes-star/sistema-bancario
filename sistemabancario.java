abstract class ContaBancaria {
    private String titular;
    private double saldo;

    public ContaBancaria(String titular, double saldoInicial) {
        this.titular = titular;
        this.saldo = saldoInicial;
    }

    public String getTitular() {
        return titular;
    }

    public double getSaldo() {
        return saldo;
    }

    protected void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public void depositar(double valor) {
        if (valor > 0) {
            saldo += valor;
            System.out.println("Deposito de R$" + valor + " realizado.");
        } else {
            System.out.println("Valor de deposito invalido.");
        }
    }

    public boolean sacar(double valor) {
        if (valor > 0 && valor <= saldo) {
            saldo -= valor;
            System.out.println("Saque de R$" + valor + " realizado.");
            return true;
        } else {
            System.out.println("Saque invalido: saldo insuficiente ou valor negativo.");
            return false;
        }
    }

    public abstract String extrato();
}

class ContaCorrente extends ContaBancaria {
    private double limite;

    public ContaCorrente(String titular, double saldoInicial, double limite) {
        super(titular, saldoInicial);
        this.limite = limite;
    }

    @Override
    public boolean sacar(double valor) {
        if (valor > 0 && valor <= (getSaldo() + limite)) {
            double novoSaldo = getSaldo() - valor;
            setSaldo(novoSaldo);
            System.out.println("Saque de R$" + valor + " realizado.");
            if (novoSaldo < 0) {
                System.out.println("Usando limite de cheque especial.");
            }
            return true;
        }
        System.out.println("Saque excede saldo + limite.");
        return false;
    }

    @Override
    public String extrato() {
        return "=== CONTA CORRENTE ===\n" +
               "Titular: " + getTitular() + "\n" +
               "Saldo: R$" + getSaldo() + "\n" +
               "Limite: R$" + limite + "\n" +
               "Disponivel: R$" + (getSaldo() + limite);
    }
}

class ContaPoupanca extends ContaBancaria {
    private double taxaRendimento;

    public ContaPoupanca(String titular, double saldoInicial, double taxaRendimento) {
        super(titular, saldoInicial);
        this.taxaRendimento = taxaRendimento;
    }

    public void renderJuros() {
        double juros = getSaldo() * taxaRendimento;
        depositar(juros);
        System.out.println("Rendimento de R$" + juros + " aplicado.");
    }

    @Override
    public String extrato() {
        return "=== CONTA POUPANCA ===\n" +
               "Titular: " + getTitular() + "\n" +
               "Saldo: R$" + getSaldo() + "\n" +
               "Taxa de rendimento: " + (taxaRendimento * 100) + "%";
    }
}

public class SistemaBancario {
    public static void main(String[] args) {
        ContaBancaria cc = new ContaCorrente("Joao Silva", 1000.0, 500.0);
        ContaBancaria cp = new ContaPoupanca("Maria Souza", 2000.0, 0.05);

        System.out.println("===== TESTE CONTA CORRENTE =====");
        System.out.println(cc.extrato());
        cc.depositar(500.0);
        cc.sacar(1200.0);
        System.out.println(cc.extrato());

        System.out.println("\n===== TESTE CONTA POUPANCA =====");
        System.out.println(cp.extrato());
        cp.depositar(300.0);

        if (cp instanceof ContaPoupanca) {
            ((ContaPoupanca) cp).renderJuros();
        }

        System.out.println(cp.extrato());

        System.out.println("\n===== POLIMORFISMO COM FOR-EACH =====");
        ContaBancaria[] contas = {cc, cp};
        for (ContaBancaria conta : contas) {
            System.out.println(conta.extrato());
            System.out.println("--------------------");
        }
    }
}
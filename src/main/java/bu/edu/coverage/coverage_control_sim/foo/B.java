package bu.edu.coverage.coverage_control_sim.foo;

public class B implements A {

	public void foo(C c) {
		System.out.println("foo c");

	}

	public void foo(D d) {
		System.out.println("foo d");
	}

	public static void main(String[] args) {
		E e = new E(new B());
		e.bar();
	}
}

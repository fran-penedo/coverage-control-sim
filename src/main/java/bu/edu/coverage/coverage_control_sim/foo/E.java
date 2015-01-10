package bu.edu.coverage.coverage_control_sim.foo;

public class E {
	A a;

	E(A a) {
		this.a = a;
	}

	void bar() {
		C c = new C();
		D d = new D();

		a.foo(c);
		a.foo(d);

	}

}

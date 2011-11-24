<h1>A simple &lambda;-calculus interpreter</h1> 
<p>
    A simple implementation of a &lambda;-calculus interpreter. At current moment
    it uses rewrite as method for solving &beta;-reductions. 
</p>

<h3>Basic Usage</h3>
<p> 
    Lambda calculus basically has only variables, applications and lambda 
    abstractions. To simplify language parse you have to write this way:
    <ul>
        <li>
            <code>[var, term]</code> to write a lambda abstraction of kind 
            &lambda;var.term;
        </li>
        <li>
            <code>(a, b)</code> to applicate a to b - a(b);
        </li>
        <li>
            just declare variables and they will be allocated.
        </li>
    </ul>
    This is pure &lambda;-calculus. There are just lambda terms and their beta
    reduction. Do not expect to have more than this. This interpreter has been
    done only for academic purpose.
</p>

package br.zzz.investimento.application;

public abstract class UseCase<In, Out>  {
    public abstract Out execute(In input);
}

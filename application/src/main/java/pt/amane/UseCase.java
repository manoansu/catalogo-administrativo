package pt.amane;

public abstract class UseCase<IN, OUT> {

  public abstract OUT execute(IN anIn);

}

package testReflection;

public class StringOccurrence
{
	public int times = 0;
	public InboundOutbound inboundOutbound;
	public String direction = "From LOC1";
	
	public int getTimes()
	{
		return times;
	}
	public void setTimes(int times)
	{
		this.times = times;
	}
	public InboundOutbound getInboundOutbound()
	{
		return inboundOutbound;
	}
	public void setInboundOutbound(InboundOutbound inboundOutbound)
	{
		this.inboundOutbound = inboundOutbound;
	}
	public String getDirection()
	{
		return direction;
	}
	public void setDirection(String direction)
	{
		this.direction = direction;
	}
	
	
}

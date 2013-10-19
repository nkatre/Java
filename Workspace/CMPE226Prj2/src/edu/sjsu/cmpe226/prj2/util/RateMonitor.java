package edu.sjsu.cmpe226.prj2.util;

public class RateMonitor
{
  private final String _name;
  private long _startTime;
  private long _numTicks;
  private double _rate;
  private long  _durationSoFar;
  private State _state;

  /*
   * Rate Monitor internal state
   */
  private enum State
  {
    INIT,
    STARTED,
    SUSPENDED,
    RESUMED,
    STOPPED
  }

  public RateMonitor(String name)
  {
    _name = name;
    _startTime = -1;
    _numTicks = 0;
    _state = State.INIT;
  }

  /*
   * @return true if the Monitor has been started
   */
  public boolean started()
  {
    return (_startTime > 0);
  }

  /*
   * Suspends the started/resumed monitor
   */
  public void suspend()
  {
    if ( (_state == State.STARTED ) ||
         (_state == State.RESUMED))
    {
      _durationSoFar += (System.nanoTime() - _startTime);
      _startTime = 0;
      _state = State.SUSPENDED;
    }
  }

  /*
   * Resumes the suspended monitor
   *
   */
  public void resume()
  {
    if ( _state == State.SUSPENDED)
    {
      _startTime = System.nanoTime();
      _state = State.RESUMED;
    }
  }

  /*
   * Initializes and Starts the Monitor.
   */
  public void start()
  {
    _startTime = System.nanoTime();
    _durationSoFar = 0;
    _numTicks = 0;
    _state = State.STARTED;
  }

  /*
   * Increment the monitored event by one
   */
  public void tick()
  {
    assert( ( _state != State.STOPPED ) &&
            (_state != State.SUSPENDED));
    _numTicks ++;
  }

  /*
   * Increment the monitored event by ticks
   *
   * @param ticks : Number of events to be added
   */
  public void ticks(long ticks)
  {
    assert( ( _state != State.STOPPED ) &&
            (_state != State.SUSPENDED));

    _numTicks += ticks;
  }

  /*
   * @return the rate of events discounting the time it was in suspended/stopped state
   *
   */
  public double getRate()
  {
    long duration = getDuration();

    if (_numTicks > 1000000000)
    {
      _rate = 1000000000 * ((double)_numTicks / (double) duration );
    }
    else
    {
      _rate = _numTicks * 1000000000.0 / duration;
    }
    return _rate;
  }

  /*
   * @return the latency/duration discounting the time it was in suspended/stopped state
   */
  public long getDuration()
  {
    long duration = 0;

    switch ( _state)
    {
      case STARTED   :  duration = System.nanoTime() - _startTime;
                        break;
      case RESUMED   :  duration = System.nanoTime() - _startTime + _durationSoFar;
                        break;

      case STOPPED   :
      case SUSPENDED :  duration = _durationSoFar;
                        break;
      case INIT      :  throw new RuntimeException("RateMonitor not started !!");
    }

    return duration;
  }

  /*
   * Stops the monitor. Equivalent to suspend state except that it cannot be resumed
   */
  public void stop()
  {
    if ( _state != State.SUSPENDED)
      _durationSoFar += (System.nanoTime() - _startTime);

    _state = State.STOPPED;
  }

  @Override
  public String toString()
  {
   StringBuilder sb = new StringBuilder();
   sb.append("RateMonitor:")
     .append(_name)
     .append(":Rate = ")
     .append(getRate())
     .append(":Duration = ")
     .append(getDuration())
     .append(":Ticks =")
     .append(_numTicks);

   return sb.toString();

  }

  /*
   * @return the current State of the RateMonitor
   */
  public State getState()
  {
	  return _state;
  }

  public boolean isStarted()
  {
    return _state == State.STARTED;
  }
}

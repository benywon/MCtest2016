package preprocess;

/**
 * Created by benywon on 1/28 0028.
 */
public class Feature
{
    public String getFeatureName()
    {
        return featureName;
    }

    public float getFeatureValue()
    {
        return featureValue;
    }

    public String featureName;
    public float featureValue;

    public Feature(String featureName, float featureValue)
    {
        this.featureName = featureName;
        this.featureValue = featureValue;
    }
}
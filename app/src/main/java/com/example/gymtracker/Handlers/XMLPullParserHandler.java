//package com.example.gymtracker.Handlers;
//
//import com.example.gymtracker.Model.ExerciseData;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//import org.xmlpull.v1.XmlPullParser;
//import org.xmlpull.v1.XmlPullParserException;
//import org.xmlpull.v1.XmlPullParserFactory;
//
//public class XMLPullParserHandler {
//    private List<ExerciseData> exercises = new ArrayList<ExerciseData>();
//    private ExerciseData exerciseData;
//    private String text;
//
//    public List<ExerciseData> getExercises() {
//        return exercises;
//    }
//
//    public List<ExerciseData> parse(InputStream is) {
//        try {
//            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//            factory.setNamespaceAware(true);
//            XmlPullParser  parser = factory.newPullParser();
//
//            parser.setInput(is, null);
//
//            int eventType = parser.getEventType();
//            while (eventType != XmlPullParser.END_DOCUMENT) {
//                String tagname = parser.getName();
//                switch (eventType) {
//                    case XmlPullParser.START_TAG:
//                        if (tagname.equalsIgnoreCase("exercise")) {
//                            // create a new instance of employee
//                            exerciseData = new ExerciseData();
//                        }
//                        break;
//
//                    case XmlPullParser.TEXT:
//                        text = parser.getText();
//                        break;
//
//                    case XmlPullParser.END_TAG:
//                        if (tagname.equalsIgnoreCase("exercise")) {
//                            // add employee object to list
//                            exercises.add(exerciseData);
//                        }
//                        else if (tagname.equalsIgnoreCase("title")) {
//                            exerciseData.setTitle(text);
//                        }
////                        else if (tagname.equalsIgnoreCase("id")) {
////                            exerciseData.setId(Integer.parseInt(text));
////                        }
//                        break;
//
//                    default:
//                        break;
//                }
//                eventType = parser.next();
//            }
//
//        } catch (XmlPullParserException e) {e.printStackTrace();}
//        catch (IOException e) {e.printStackTrace();}
//
//        return exercises;
//    }
//}

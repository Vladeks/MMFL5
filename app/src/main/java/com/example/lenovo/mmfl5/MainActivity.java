package com.example.lenovo.mmfl5;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.*;

import android.os.Bundle;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import android.content.Context;
import android.content.res.Resources.Theme;
import com.example.lenovo.mmfl5.Operations.*;
import com.example.lenovo.mmfl5.Source.*;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Arrays;
import java.util.Comparator;

public class MainActivity
        extends AppCompatActivity{

    private EditText etA;
    private EditText etB;
    private EditText etC;
    private EditText etD;

    private EditText etAnumberB;
    private EditText etBnumberB;
    private EditText etCnumberB;
    private EditText etDnumberB;

    private EditText etAthre;
    private EditText etBthre;
    private EditText etCthre;

    private EditText etAthreB;
    private EditText etBthreB;
    private EditText etCthreB;

    private EditText etAtwo;
    private EditText etBtwo;

    private EditText etAtwoB;
    private EditText etBtwoB;

    private EditText etSteps;

    private View viewThreeNumbers;
    private View viewTwoNumbers;
    private View viewFourNumbers;

    private View viewThreeB;
    private View viewTwoB;
    private View viewFourB;
    private View frameLayoutB;

    private GraphView graph;

    private SwitchCompat switcherOperation;
    private SwitchCompat switcherRounded;

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    private ActionBarDrawerToggle drawerToggle;

    private Spinner spinnerOperation;

    private AffiliationFunction function;
    private Operation operation;

    private FloatingActionButton fab;
    private RecyclerView mRecyclerView;
    private BottomSheetBehavior bottomSheetBehavior;

    private final String TAG_FUNCTION = "FUNCTION";
    private final String TAG_OPERATION = "OPERATION";

    private  DataPoint[] dpA ;
    private  DataPoint[] dpB ;
    private  DataPoint[] dpC ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open,  R.string.drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);

        // Setup spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new MyAdapter(
                toolbar.getContext(),
                new String[]{
                        getString(R.string.function_trapezoid),
                        getString(R.string.function_two_side_gauss),
                        getString(R.string.function_difference_sigmoid),
                        getString(R.string.function_generalized),
                        getString(R.string.function_pi_look),
                        getString(R.string.function_product_sigmoid),
                        getString(R.string.function_sigmoid),
                        getString(R.string.function_s_look),
                        getString(R.string.function_square),
                        getString(R.string.function_symmetric_gauss),
                        getString(R.string.function_triangular),
                        getString(R.string.function_z_look),
                        getString(R.string.function_laplass),
                }));

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // When the given dropdown item is selected, show its contents in the
                // container view.
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))

                function = getSelectedItem(position);
                graph.removeAllSeries();
                Log.d(TAG_FUNCTION, function.getClass().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        initializeNavigationMenu();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!switcherOperation.isChecked()) {
                    AffiliationFunction fun = getFunction(view);

                    if(fun == null) {
                        Log.d(TAG_FUNCTION, "fun = null ");
                        return;
                    }
                    graph.removeAllSeries();
                    drawGraph(graph, fun);
                    Snackbar.make(view, "Build", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    AffiliationFunction funA = getFunction(view);
                    AffiliationFunction funB = getFunctionB(view);
                    if(funA == null || funB == null) {
                        Log.d(TAG_FUNCTION, "fun = null ");
                        return;
                    }

                    if(operation == null){
                        Log.d(TAG_OPERATION, "operation = null ");
                        return;
                    }


                    int step = getStepSizeFromView(etSteps, view);
                    if(step == -1){
                        Snackbar.make(view, "Incorrect step", Snackbar.LENGTH_LONG).show();
                        Log.d(TAG_OPERATION, "step = -1 ");
                        return;
                    }

                    graph.removeAllSeries();
                    double min = operation.calculateMin(funA, funB);
                    double max = operation.calculateMax(funA, funB);
                    double stepSize = operation.calculateStepSize(funA, funB, step);
                    dpA = operation.calculateFunctionPoints(funA, min, max, stepSize);
                    dpB = operation.calculateFunctionPoints(funB, min, max, stepSize);


                    if(switcherRounded.isChecked()){
                        dpC = operation.execute(funA, funB);
                        Log.d(TAG_OPERATION, Arrays.toString(operation.execute(funA, funB)));
                    } else {
                        dpC = operation.execute(funA, funB, step);
                        Log.d(TAG_OPERATION, Arrays.toString(operation.execute(funA, funB, step)));
                    }
                    drawGraph(graph, dpA, dpB, dpC);
                    initButtonSheet(dpC);

                    //Snackbar.make(view, "Operation", Snackbar.LENGTH_LONG).show();
                }

            }
        });


    }

    private void initializeNavigationMenu() {
        Menu menu = navigationView.getMenu();
        MenuItem menuItemRounded = menu.findItem(R.id.nav_rounded);
        View actionViewRounded = MenuItemCompat.getActionView(menuItemRounded);
        switcherRounded = (SwitchCompat) actionViewRounded.findViewById(R.id.switchRounded);
        switcherRounded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switcherRounded.isChecked()){
                    switcherRounded.setText(R.string.rounded);
                }else {
                    switcherRounded.setText(R.string.full_result);
                }
                Snackbar.make(v, (switcherRounded.isChecked()) ? R.string.rounded :
                        R.string.full_result, Snackbar.LENGTH_SHORT).show();
            }
        });

        MenuItem menuItemStep = menu.findItem(R.id.nav_steps);
        View actionViewStep = MenuItemCompat.getActionView(menuItemStep);
        etSteps = (EditText) actionViewStep.findViewById(R.id.etSteps);

        MenuItem menuItemOperation = menu.findItem(R.id.nav_operation);
        View actionViewOperation = MenuItemCompat.getActionView(menuItemOperation);
        spinnerOperation =(Spinner) actionViewOperation.findViewById(R.id.spinnerOperationSelector);
        spinnerOperation.setAdapter(new MyAdapter(navigationView.getContext(),
                new String[]{
                        getString(R.string.operation_add),
                        getString(R.string.operation_subtract),
                        getString(R.string.operation_multiply),
                        getString(R.string.operation_divide),
                        getString(R.string.operation_maximum),
                        getString(R.string.operation_minimum),
                }));
        spinnerOperation.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                operation = getOperation(i);
                Log.d(TAG_OPERATION, operation.getClass().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        View headerLayout = navigationView.getHeaderView(0);
        final TextView result = headerLayout.findViewById(R.id.result);

        MenuItem menuItemFind = menu.findItem(R.id.nav_find);
        View actionViewFind = MenuItemCompat.getActionView(menuItemFind);
        final EditText etX = actionViewFind.findViewById(R.id.etX);
//        final TextView twAX = actionViewFind.findViewById(R.id.twAX);
//        final TextView twBX = actionViewFind.findViewById(R.id.twBX);
//        final TextView twCX = actionViewFind.findViewById(R.id.twCX);
        Button btnFind =  actionViewFind.findViewById(R.id.btnFind);
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AffiliationFunction funA = getFunction(view);
                AffiliationFunction funB = getFunctionB(view);
                if(dpC!= null && !etX.getText().toString().equals("")){
                    int i = Arrays.binarySearch(dpC, new DataPoint(getDecimalValue(etX), 0), new Comparator<DataPoint>() {
                        @Override
                        public int compare(DataPoint dataPoint, DataPoint t1) {
                            return Double.compare(dataPoint.getX(), t1.getX());
                        }
                    });
                    System.out.println(i);
                    String answer = getString(R.string.m_a_x)
                            .concat(String.valueOf(funA.calculateAffiliationFunction(getDecimalValue(etX))))
                            .concat("\n" + getString(R.string.m_b_x))
                            .concat(String.valueOf(funB.calculateAffiliationFunction(getDecimalValue(etX))));
                    result.setText(answer);
//                    twAX.setText(String.valueOf(funA.calculateAffiliationFunction(getDecimalValue(etX))));
//                    twBX.setText(String.valueOf(funB.calculateAffiliationFunction(getDecimalValue(etX))));
                    //twCX.setText(String.valueOf(dpC[i].getY()));
                } else {
                    Snackbar.make(view, "incorrect params", Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }

    private void initButtonSheet(DataPoint[] datapoin) {
        // получение вью нижнего экрана
        LinearLayout llBottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);

// настройка поведения нижнего экрана
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);

// настройка состояний нижнего экрана
//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//        app:layout_anchor="@+id/bottom_sheet"
//        app:layout_anchorGravity="top|end"
//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
//        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//
//                // этот код скрывает кнопку сразу же
//                // и отображает после того как нижний экран полностью свернется
//                if (BottomSheetBehavior.STATE_DRAGGING == newState) {
//                    fab.animate().scaleX(0).scaleY(0).setDuration(300).start();
//                } else if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
//                    fab.animate().scaleX(1).scaleY(1).setDuration(300).start();
//                }
//            }
//
//            @Override
//            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//            }
//        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recycleView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        RvAdapter mAdapter = new RvAdapter(datapoin);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initViews() {
        viewFourNumbers = findViewById(R.id.view_four_number);
        viewThreeNumbers = findViewById(R.id.view_three_number);
        viewTwoNumbers = findViewById(R.id.view_two_number);

        viewFourB = findViewById(R.id.view_four_b);
        viewThreeB = findViewById(R.id.view_three_b);
        viewTwoB = findViewById(R.id.view_two_b);

        frameLayoutB = findViewById(R.id.frameLayoutB);

        etA = (EditText) findViewById(R.id.etAnumber);
        etB = (EditText) findViewById(R.id.etBnumber);
        etC = (EditText) findViewById(R.id.etCnumber);
        etD = (EditText) findViewById(R.id.etDnumber);

        etAnumberB = (EditText) findViewById(R.id.etAnumberB);
        etBnumberB = (EditText) findViewById(R.id.etBnumberB);
        etCnumberB = (EditText) findViewById(R.id.etCnumberB);
        etDnumberB = (EditText) findViewById(R.id.etDnumberB);

        etAthre = (EditText) findViewById(R.id.etAnumTtree);
        etBthre = (EditText) findViewById(R.id.etBnumTtree);
        etCthre = (EditText) findViewById(R.id.etCnumTree);

        etAthreB = (EditText) findViewById(R.id.etAnumTtreeB);
        etBthreB = (EditText) findViewById(R.id.etBnumTtreeB);
        etCthreB = (EditText) findViewById(R.id.etCnumTreeB);

        etAtwo = (EditText) findViewById(R.id.etAnumTwo);
        etBtwo = (EditText) findViewById(R.id.etBnumTwo);

        etAtwoB = (EditText) findViewById(R.id.etAnumTwoB);
        etBtwoB = (EditText) findViewById(R.id.etBnumTwoB);

        graph = (GraphView) findViewById(R.id.graph);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Синхронизировать состояние переключения после того, как
        // возникнет onRestoreInstanceState
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Передать любые изменения конфигурации переключателям
        // drawer'а
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private Operation getOperation(int id) {
        switch (id) {
            case 0: return new AddingOperation();
            case 1: return new SubtractingOperation();
            case 2: return new MultiplyOperation();
            case 3: return new DividindOperation();
            case 4: return new MaximumOperation();
            case 5: return new MinimumOperation();
        }
        return null;
    }

    private AffiliationFunction getSelectedItem(int id) {
        switch (id){
            case 0: viewFourNumbers.setVisibility(View.VISIBLE);
                viewThreeNumbers.setVisibility(View.GONE);
                viewTwoNumbers.setVisibility(View.GONE);

                viewFourB.setVisibility(View.VISIBLE);
                viewThreeB.setVisibility(View.GONE);
                viewTwoB.setVisibility(View.GONE);
                return new TrapezoidAffiliationFunction();

            case 1: viewFourNumbers.setVisibility(View.VISIBLE);
                viewThreeNumbers.setVisibility(View.GONE);
                viewTwoNumbers.setVisibility(View.GONE);

                viewFourB.setVisibility(View.VISIBLE);
                viewThreeB.setVisibility(View.GONE);
                viewTwoB.setVisibility(View.GONE);
                return new TwoSideGaussAffiliationFunction();

            case 2: viewFourNumbers.setVisibility(View.VISIBLE);
                viewThreeNumbers.setVisibility(View.GONE);
                viewTwoNumbers.setVisibility(View.GONE);

                viewFourB.setVisibility(View.VISIBLE);
                viewThreeB.setVisibility(View.GONE);
                viewTwoB.setVisibility(View.GONE);
                return new DifferenceSigmoidAffiliationFunction();

            case 3: viewThreeNumbers.setVisibility(View.VISIBLE);
                viewFourNumbers.setVisibility(View.GONE);
                viewTwoNumbers.setVisibility(View.GONE);

                viewThreeB.setVisibility(View.VISIBLE);
                viewFourB.setVisibility(View.GONE);
                viewTwoB.setVisibility(View.GONE);
                return new GeneralizedAffiliationFunction();

            case 4: viewFourNumbers.setVisibility(View.VISIBLE);
                viewThreeNumbers.setVisibility(View.GONE);
                viewTwoNumbers.setVisibility(View.GONE);

                viewFourB.setVisibility(View.VISIBLE);
                viewThreeB.setVisibility(View.GONE);
                viewTwoB.setVisibility(View.GONE);
                return new PiLookAlikeAffiliationFunction();

            case 5: viewFourNumbers.setVisibility(View.VISIBLE);
                viewThreeNumbers.setVisibility(View.GONE);
                viewTwoNumbers.setVisibility(View.GONE);

                viewFourB.setVisibility(View.VISIBLE);
                viewThreeB.setVisibility(View.GONE);
                viewTwoB.setVisibility(View.GONE);
                return new ProductSigmoidAffiliationFunction();

            case 6: viewTwoNumbers.setVisibility(View.VISIBLE);
                viewFourNumbers.setVisibility(View.GONE);
                viewThreeNumbers.setVisibility(View.GONE);

                viewTwoB.setVisibility(View.VISIBLE);
                viewFourB.setVisibility(View.GONE);
                viewThreeB.setVisibility(View.GONE);
                return new SigmoidAffiliationFunction();

            case 7: viewTwoNumbers.setVisibility(View.VISIBLE);
                viewFourNumbers.setVisibility(View.GONE);
                viewThreeNumbers.setVisibility(View.GONE);

                viewTwoB.setVisibility(View.VISIBLE);
                viewFourB.setVisibility(View.GONE);
                viewThreeB.setVisibility(View.GONE);
                return new SLookAlikeAffiliationFunction();

            case 8: viewTwoNumbers.setVisibility(View.VISIBLE);
                viewFourNumbers.setVisibility(View.GONE);
                viewThreeNumbers.setVisibility(View.GONE);

                viewTwoB.setVisibility(View.VISIBLE);
                viewFourB.setVisibility(View.GONE);
                viewThreeB.setVisibility(View.GONE);
                return new SquareAffiliationFunction();

            case 9: viewTwoNumbers.setVisibility(View.VISIBLE);
                viewFourNumbers.setVisibility(View.GONE);
                viewThreeNumbers.setVisibility(View.GONE);

                viewTwoB.setVisibility(View.VISIBLE);
                viewFourB.setVisibility(View.GONE);
                viewThreeB.setVisibility(View.GONE);
                return new SymmetricGaussAffiliationFunction();

            case 10: viewThreeNumbers.setVisibility(View.VISIBLE);
                viewFourNumbers.setVisibility(View.GONE);
                viewTwoNumbers.setVisibility(View.GONE);

                viewThreeB.setVisibility(View.VISIBLE);
                viewFourB.setVisibility(View.GONE);
                viewTwoB.setVisibility(View.GONE);
                return new TriangularAffiliationFunction();

            case 11: viewTwoNumbers.setVisibility(View.VISIBLE);
                viewFourNumbers.setVisibility(View.GONE);
                viewThreeNumbers.setVisibility(View.GONE);

                viewTwoB.setVisibility(View.VISIBLE);
                viewFourB.setVisibility(View.GONE);
                viewThreeB.setVisibility(View.GONE);
                return new ZLookAlikeAffiliationFunction();

            case 12: viewTwoNumbers.setVisibility(View.VISIBLE);
                viewThreeNumbers.setVisibility(View.GONE);
                viewFourNumbers.setVisibility(View.GONE);

                viewTwoB.setVisibility(View.VISIBLE);
                viewThreeB.setVisibility(View.GONE);
                viewFourB.setVisibility(View.GONE);
                return new LaplassAffiliationFunction();

        }
        return null;
    }

    private AffiliationFunction getFunction(View view) {
        double[] p;
        if(function.getClass() == TrapezoidAffiliationFunction.class) {
            p = getIntervalFromView(etA, etB, etC, etD, view, new TrapezoidAffiliationFunction());
            if(p == null) {
                Log.d(TAG_FUNCTION, "p = null");
                return null;
            }
            Log.d(TAG_FUNCTION, "p=" + Arrays.toString(p));
            return new TrapezoidAffiliationFunction(p[0], p[1], p[2], p[3]);
        }
        else if(function.getClass() == TwoSideGaussAffiliationFunction.class) {
            p = getIntervalFromView(etA, etB, etC, etD, view, new TwoSideGaussAffiliationFunction());
            if(p == null) {
                Log.d(TAG_FUNCTION, "p = null");
                return null;
            }
            Log.d(TAG_FUNCTION, "p=" + Arrays.toString(p));
            return new TwoSideGaussAffiliationFunction(p[0], p[1], p[2], p[3]);
        }
        else if(function.getClass() == DifferenceSigmoidAffiliationFunction.class) {
            p = getIntervalFromView(etA, etB, etC, etD, view, new DifferenceSigmoidAffiliationFunction());
            if(p == null) {
                Log.d(TAG_FUNCTION, "p = null");
                return null;
            }
            Log.d(TAG_FUNCTION, "p=" + Arrays.toString(p));
            return new DifferenceSigmoidAffiliationFunction(p[0], p[1], p[2], p[3]);
        }
        else if(function.getClass() == SquareAffiliationFunction.class) {
            p = getIntervalFromView(etAtwo, etBtwo, view, new SquareAffiliationFunction());
            if(p == null) {
                Log.d(TAG_FUNCTION, "p = null");
                return null;
            }
            Log.d(TAG_FUNCTION, "p=" + Arrays.toString(p));
            return new SquareAffiliationFunction(p[0], p[1]);
        }
        else if(function.getClass() == GeneralizedAffiliationFunction.class) {
            p = getIntervalFromView(etAthre, etBthre, etCthre, view, new GeneralizedAffiliationFunction());
            if(p == null) {
                Log.d(TAG_FUNCTION, "p = null");
                return null;
            }
            Log.d(TAG_FUNCTION, "p=" + Arrays.toString(p));
            return new GeneralizedAffiliationFunction(p[0], p[1], p[2]);
        }
        else if(function.getClass() == SymmetricGaussAffiliationFunction.class) {
            p = getIntervalFromView(etAthre, etBthre, etCthre, view, new SymmetricGaussAffiliationFunction());
            if(p == null) {
                Log.d(TAG_FUNCTION, "p = null");
                return null;
            }
            Log.d(TAG_FUNCTION, "p=" + Arrays.toString(p));
            return new SymmetricGaussAffiliationFunction(p[0], p[1]);
        }
        else if(function.getClass() == PiLookAlikeAffiliationFunction.class) {
            p = getIntervalFromView(etA, etB, etC, etD, view, new PiLookAlikeAffiliationFunction());
            if(p == null) {
                Log.d(TAG_FUNCTION, "p = null");
                return null;
            }
            Log.d(TAG_FUNCTION, "p=" + Arrays.toString(p));
            return new PiLookAlikeAffiliationFunction(p[0], p[1], p[2], p[3]);
        }
        else if(function.getClass() == TriangularAffiliationFunction.class) {
            p = getIntervalFromView(etAthre, etBthre, etCthre, view, new TriangularAffiliationFunction());
            if(p == null) {
                Log.d(TAG_FUNCTION, "p = null");
                return null;
            }
            Log.d(TAG_FUNCTION, "p=" + Arrays.toString(p));
            return new TriangularAffiliationFunction(p[0], p[1], p[2]);
        }
        else if(function.getClass() == ProductSigmoidAffiliationFunction.class) {
            p = getIntervalFromView(etA, etB, etC, etD, view, new ProductSigmoidAffiliationFunction());
            if(p == null) {
                Log.d(TAG_FUNCTION, "p = null");
                return null;
            }
            Log.d(TAG_FUNCTION, "p=" + Arrays.toString(p));
            return new ProductSigmoidAffiliationFunction(p[0], p[1], p[2], p[3]);
        }
        else if(function.getClass() == SigmoidAffiliationFunction.class) {
            p = getIntervalFromView(etAtwo, etBtwo, view, new SigmoidAffiliationFunction());
            if(p == null) {
                Log.d(TAG_FUNCTION, "p = null");
                return null;
            }
            Log.d(TAG_FUNCTION, "p=" + Arrays.toString(p));
            return new SigmoidAffiliationFunction(p[0], p[1]);
        } else if(function.getClass() == SLookAlikeAffiliationFunction.class) {
            p = getIntervalFromView(etAtwo, etBtwo, view,new SLookAlikeAffiliationFunction());
            if(p == null) {
                Log.d(TAG_FUNCTION, "p = null");
                return null;
            }
            Log.d(TAG_FUNCTION, "p=" + Arrays.toString(p));
            return new SLookAlikeAffiliationFunction(p[0], p[1]);
        }
        else if(function.getClass() == ZLookAlikeAffiliationFunction.class) {
            p = getIntervalFromView(etAtwo, etBtwo, view, new ZLookAlikeAffiliationFunction());
            if(p == null) {
                Log.d(TAG_FUNCTION, "p = null");
                return null;
            }
            Log.d(TAG_FUNCTION, "p=" + Arrays.toString(p));
            return new ZLookAlikeAffiliationFunction(p[0], p[1]);
        }
        else if(function.getClass() == LaplassAffiliationFunction.class) {
            p = getIntervalFromView(etAtwo, etBtwo, view, new LaplassAffiliationFunction());
            if(p == null) {
                Log.d(TAG_FUNCTION, "p = null");
                return null;
            }
            Log.d(TAG_FUNCTION, "p=" + Arrays.toString(p));
            return new LaplassAffiliationFunction(p[0], p[1]);
        }
        return null;
    }

    private AffiliationFunction getFunctionB(View view) {
        double[] p;
        if(function.getClass() == TrapezoidAffiliationFunction.class) {
            p = getIntervalFromView(etAnumberB, etBnumberB, etCnumberB, etDnumberB, view, new TrapezoidAffiliationFunction());
            if(p == null) {
                Log.d(TAG_FUNCTION, "p = null");
                return null;
            }
            Log.d(TAG_FUNCTION, "p=" + Arrays.toString(p));
            return new TrapezoidAffiliationFunction(p[0], p[1], p[2], p[3]);
        }
        else if(function.getClass() == TwoSideGaussAffiliationFunction.class) {
            p = getIntervalFromView(etAnumberB, etBnumberB, etCnumberB, etDnumberB, view, new TwoSideGaussAffiliationFunction());
            if(p == null) {
                Log.d(TAG_FUNCTION, "p = null");
                return null;
            }
            Log.d(TAG_FUNCTION, "p=" + Arrays.toString(p));
            return new TwoSideGaussAffiliationFunction(p[0], p[1], p[2], p[3]);
        }
        else if(function.getClass() == DifferenceSigmoidAffiliationFunction.class) {
            p = getIntervalFromView(etAnumberB, etBnumberB, etCnumberB, etDnumberB, view, new DifferenceSigmoidAffiliationFunction());
            if(p == null) {
                Log.d(TAG_FUNCTION, "p = null");
                return null;
            }
            Log.d(TAG_FUNCTION, "p=" + Arrays.toString(p));
            return new DifferenceSigmoidAffiliationFunction(p[0], p[1], p[2], p[3]);
        }
        else if(function.getClass() == SquareAffiliationFunction.class) {
            p = getIntervalFromView(etAtwoB, etBtwoB, view, new SquareAffiliationFunction());
            if(p == null) {
                Log.d(TAG_FUNCTION, "p = null");
                return null;
            }
            Log.d(TAG_FUNCTION, "p=" + Arrays.toString(p));
            return new SquareAffiliationFunction(p[0], p[1]);
        }
        else if(function.getClass() == GeneralizedAffiliationFunction.class) {
            p = getIntervalFromView(etAthreB, etBthreB, etCthreB, view, new GeneralizedAffiliationFunction());
            if(p == null) {
                Log.d(TAG_FUNCTION, "p = null");
                return null;
            }
            Log.d(TAG_FUNCTION, "p=" + Arrays.toString(p));
            return new GeneralizedAffiliationFunction(p[0], p[1], p[2]);
        }
        else if(function.getClass() == SymmetricGaussAffiliationFunction.class) {
            p = getIntervalFromView(etAthreB, etBthreB, etCthreB, view, new SymmetricGaussAffiliationFunction());
            if(p == null) {
                Log.d(TAG_FUNCTION, "p = null");
                return null;
            }
            Log.d(TAG_FUNCTION, "p=" + Arrays.toString(p));
            return new SymmetricGaussAffiliationFunction(p[0], p[1]);
        }
        else if(function.getClass() == PiLookAlikeAffiliationFunction.class) {
            p = getIntervalFromView(etAnumberB, etBnumberB, etCnumberB, etDnumberB, view, new PiLookAlikeAffiliationFunction());
            if(p == null) {
                Log.d(TAG_FUNCTION, "p = null");
                return null;
            }
            Log.d(TAG_FUNCTION, "p=" + Arrays.toString(p));
            return new PiLookAlikeAffiliationFunction(p[0], p[1], p[2], p[3]);
        }
        else if(function.getClass() == TriangularAffiliationFunction.class) {
            p = getIntervalFromView(etAthreB, etBthreB, etCthreB, view, new TriangularAffiliationFunction());
            if(p == null) {
                Log.d(TAG_FUNCTION, "p = null");
                return null;
            }
            Log.d(TAG_FUNCTION, "p=" + Arrays.toString(p));
            return new TriangularAffiliationFunction(p[0], p[1], p[2]);
        }
        else if(function.getClass() == ProductSigmoidAffiliationFunction.class) {
            p = getIntervalFromView(etAnumberB, etBnumberB, etCnumberB, etDnumberB, view, new ProductSigmoidAffiliationFunction());
            if(p == null) {
                Log.d(TAG_FUNCTION, "p = null");
                return null;
            }
            Log.d(TAG_FUNCTION, "p=" + Arrays.toString(p));
            return new ProductSigmoidAffiliationFunction(p[0], p[1], p[2], p[3]);
        }
        else if(function.getClass() == SigmoidAffiliationFunction.class) {
            p = getIntervalFromView(etAtwoB, etBtwoB, view, new SigmoidAffiliationFunction());
            if(p == null) {
                Log.d(TAG_FUNCTION, "p = null");
                return null;
            }
            Log.d(TAG_FUNCTION, "p=" + Arrays.toString(p));
            return new SigmoidAffiliationFunction(p[0], p[1]);
        } else if(function.getClass() == SLookAlikeAffiliationFunction.class) {
            p = getIntervalFromView(etAtwoB, etBtwoB, view,new SLookAlikeAffiliationFunction());
            if(p == null) {
                Log.d(TAG_FUNCTION, "p = null");
                return null;
            }
            Log.d(TAG_FUNCTION, "p=" + Arrays.toString(p));
            return new SLookAlikeAffiliationFunction(p[0], p[1]);
        }
        else if(function.getClass() == ZLookAlikeAffiliationFunction.class) {
            p = getIntervalFromView(etAtwoB, etBtwoB, view, new ZLookAlikeAffiliationFunction());
            if(p == null) {
                Log.d(TAG_FUNCTION, "p = null");
                return null;
            }
            Log.d(TAG_FUNCTION, "p=" + Arrays.toString(p));
            return new ZLookAlikeAffiliationFunction(p[0], p[1]);
        }
        else if(function.getClass() == LaplassAffiliationFunction.class) {
            p = getIntervalFromView(etAtwoB, etBtwoB, view, new LaplassAffiliationFunction());
            if(p == null) {
                Log.d(TAG_FUNCTION, "p = null");
                return null;
            }
            Log.d(TAG_FUNCTION, "p=" + Arrays.toString(p));
            return new LaplassAffiliationFunction(p[0], p[1]);
        }
        return null;
    }

    private void drawGraph(GraphView graph, AffiliationFunction function) {
        System.out.println("Draw Graph");
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMaxY(1.2);
        graph.getViewport().setMinY(-0.1);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMaxX(function.getMaxX());
        graph.getViewport().setMinX(function.getMinX());

        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);

        DataPoint[] data = calculateFunctionPoints(function);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(data);
        series.setThickness(10);
        graph.addSeries(series);

    }

    private void drawGraph(GraphView graph, DataPoint[] functionA, DataPoint[] functionB, DataPoint[] functionC) {
        System.out.println("Draw Operation");
        graph.removeAllSeries();
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMaxY(1.2);
        graph.getViewport().setMinY(-0.1);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMaxX(20);
        graph.getViewport().setMinX(0);

        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);

        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        LineGraphSeries<DataPoint> seriesA = new LineGraphSeries<>(functionA);
        seriesA.setColor(Color.GREEN);
        seriesA.setTitle("A");
        LineGraphSeries<DataPoint> seriesB = new LineGraphSeries<>(functionB);
        seriesB.setColor(Color.BLUE);
        seriesB.setTitle("B");
        LineGraphSeries<DataPoint> seriesC = new LineGraphSeries<>(functionC);
        seriesC.setColor(Color.RED);
        seriesC.setTitle("Result");

        graph.addSeries(seriesA);
        graph.addSeries(seriesB);
        graph.addSeries(seriesC);

    }

    private DataPoint[] calculateFunctionPoints(AffiliationFunction function) {
        int count = (int) Math.round((function.getMaxX() - function.getMinX()) / function.DELTA);
        DataPoint[] data = new DataPoint[count];
        double x = function.getMinX();
        double y;
        for (int i = 0; i < count; i++) {
            y = function.calculateAffiliationFunction(x);
            data[i] = new DataPoint(x, y);
            x += function.DELTA;
        }
        return data;
    }

    private double[] getIntervalFromView(EditText etA, EditText etB, EditText etC, EditText etD, View view, AffiliationFunction function){
        if(etA.getText().toString().equals("") ||
                etB.getText().toString().equals("") ||
                etC.getText().toString().equals("") ||
                etD.getText().toString().equals("")) {
            Snackbar.make(view, getString(R.string.error_field_is_empty), Snackbar.LENGTH_LONG).show();
            return null;
        } else {
            double a = getDecimalValue(etA);
            double b = getDecimalValue(etB);
            double c = getDecimalValue(etC);
            double d = getDecimalValue(etD);

            double[] result = new double[]{a ,b, c, d};

            if(function.checkConstrains(result)) {
                return result;
            } else {
                Snackbar.make(view, getString(R.string.error_incorrect_constraints), Snackbar.LENGTH_LONG).show();
                return null;
            }
        }
    }

    private double[] getIntervalFromView(EditText etA, EditText etB, EditText etC, View view, AffiliationFunction function){
        if(etA.getText().toString().equals("") ||
                etB.getText().toString().equals("") ||
                etC.getText().toString().equals("")) {
            Snackbar.make(view, getString(R.string.error_field_is_empty), Snackbar.LENGTH_LONG).show();
            return null;
        } else {
            double a = getDecimalValue(etA);
            double b = getDecimalValue(etB);
            double c = getDecimalValue(etC);

            double[] result = new double[]{a ,b, c};

            if(function.checkConstrains(result)) {
                return result;
            } else {
                Snackbar.make(view, getString(R.string.error_incorrect_constraints), Snackbar.LENGTH_LONG).show();
                return null;
            }
        }
    }

    private double[] getIntervalFromView(EditText etA, EditText etB, View view, AffiliationFunction function){
        if(etA.getText().toString().equals("") ||
                etB.getText().toString().equals("")) {
            Snackbar.make(view, getString(R.string.error_field_is_empty), Snackbar.LENGTH_LONG).show();
            return null;
        } else {
            double a = getDecimalValue(etA);
            double b = getDecimalValue(etB);

            double[] result = new double[]{a ,b};

            if(function.checkConstrains(result)) {
                return result;
            } else {
                Snackbar.make(view, getString(R.string.error_incorrect_constraints), Snackbar.LENGTH_LONG).show();
                return null;
            }
        }
    }

    private int getStepSizeFromView(EditText etSize, View view){
        if(etSize.getText().toString().equals("")) {
            Snackbar.make(view, getString(R.string.error_field_is_empty), Snackbar.LENGTH_LONG).show();
            return -1;
        } else {
            int size = (int) getDecimalValue(etSize);

            if(size < 0) {
                Snackbar.make(view, getString(R.string.error_incorrect_constraints), Snackbar.LENGTH_LONG).show();
                return -1;
            } else {
                return size;
            }
        }
    }

    private double getDecimalValue(EditText editText) {
        return Double.parseDouble(editText.getText().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem menuItem = menu.findItem(R.id.nav_switch);
        View actionView = MenuItemCompat.getActionView(menuItem);

        switcherOperation = (SwitchCompat) actionView.findViewById(R.id.switcher);
        switcherOperation.setChecked(false);
        switcherOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switcherOperation.isChecked()){
                    frameLayoutB.setVisibility(View.VISIBLE);
                }else {
                    frameLayoutB.setVisibility(View.GONE);
                }
                Snackbar.make(v, (switcherOperation.isChecked()) ? R.string.operation_enabler :
                        R.string.operation_not_enabler, Snackbar.LENGTH_SHORT).show();
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_switch) {
            switcherOperation.setChecked(!switcherOperation.isChecked());

            if(switcherOperation.isChecked()){
                frameLayoutB.setVisibility(View.VISIBLE);
            }else {
                frameLayoutB.setVisibility(View.GONE);
            }
            Snackbar.make(frameLayoutB, (switcherOperation.isChecked()) ? R.string.operation_enabler :
                    R.string.operation_not_enabler, Snackbar.LENGTH_SHORT).show();

//            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//            drawer.closeDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static class MyAdapter extends ArrayAdapter<String> implements ThemedSpinnerAdapter {
        private final ThemedSpinnerAdapter.Helper mDropDownHelper;

        public MyAdapter(Context context, String[] objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
            mDropDownHelper = new ThemedSpinnerAdapter.Helper(context);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                // Inflate the drop down using the helper's LayoutInflater
                LayoutInflater inflater = mDropDownHelper.getDropDownViewInflater();
                view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            } else {
                view = convertView;
            }

            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getItem(position));

            return view;
        }

        @Override
        public Theme getDropDownViewTheme() {
            return mDropDownHelper.getDropDownViewTheme();
        }

        @Override
        public void setDropDownViewTheme(Theme theme) {
            mDropDownHelper.setDropDownViewTheme(theme);
        }
    }

    public static class RvAdapter extends RecyclerView.Adapter<RvAdapter.ViewHolder> {
        private DataPoint[] mDataset;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public static class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView mTextView;
            public ViewHolder(View v) {
                super(v);
                mTextView = (TextView)v.findViewById(R.id.twCord);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public RvAdapter(DataPoint[] myDataset) {
            mDataset = myDataset;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public RvAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.my_text_view, parent, false);

            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.mTextView.setText(mDataset[position].toString());

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.length;
        }
    }

}

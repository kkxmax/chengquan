//
//  SelectEvaluatorVC.h
//  chengxin
//
//  Created by seniorcoder on 11/12/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "WriteEvalViewController.h"

@interface SelectEvaluatorVC : UIViewController<UITableViewDelegate, UITableViewDataSource>
@property (strong, nonatomic) IBOutlet UITableView *tblSelectView;
@property (nonatomic, assign) NSInteger selectType;
@property (nonatomic, strong) WriteEvalViewController *rootVC;
@end

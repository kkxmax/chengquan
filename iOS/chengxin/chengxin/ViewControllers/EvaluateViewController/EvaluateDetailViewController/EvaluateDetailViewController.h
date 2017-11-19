//
//  EvaluateDetailViewController.h
//  chengxin
//
//  Created by seniorcoder on 11/3/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface EvaluateDetailViewController : UIViewController <UITableViewDelegate, UITableViewDataSource>
@property (weak, nonatomic) IBOutlet UIView *viewBlank;
@property (weak, nonatomic) IBOutlet UITableView *tblEvalView;
@property (nonatomic, strong) NSMutableDictionary *dicEvalData;
- (IBAction)onBackAction:(id)sender;

@end

//
//  EvaluateDetailViewController.h
//  chengxin
//
//  Created by seniorcoder on 11/3/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>
@protocol HotEstimationListDelegate<NSObject>
@optional

- (void)changeHotEstimationListData:(NSMutableDictionary *)hotEstimationDic;
@end

@interface EvaluateDetailViewController : UIViewController <UITableViewDelegate, UITableViewDataSource>
{
    id<HotEstimationListDelegate> hotEstimationListDelegate;
}

@property (nonatomic, retain) id<HotEstimationListDelegate> hotEstimationListDelegate;

@property (weak, nonatomic) IBOutlet UIView *viewBlank;
@property (weak, nonatomic) IBOutlet UITableView *tblEvalView;
@property (nonatomic, strong) NSMutableDictionary *dicEvalData;
- (IBAction)onBackAction:(id)sender;
@property (nonatomic, strong) IBOutlet UIView *editBackgroundView;
@property (nonatomic, strong) IBOutlet UITextField *feedbackTextField;
@property (nonatomic) BOOL isHotEvaluator;
@end

//
//  EvaluateDetailTableViewCell.h
//  chengxin
//
//  Created by seniorcoder on 11/1/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface EvaluateDetailTableViewCell : UITableViewCell

@property (nonatomic) NSInteger electCount;
@property (nonatomic) NSInteger evaluateID;
@property (nonatomic) NSInteger cellIndex;
@property (weak, nonatomic) IBOutlet UIScrollView *scrollThumb;
@property (weak, nonatomic) IBOutlet UIImageView *imgPhoto;
@property (weak, nonatomic) IBOutlet UILabel *lblTitle;
@property (weak, nonatomic) IBOutlet UILabel *lblContent;
@property (weak, nonatomic) IBOutlet UILabel *lblMore;
@property (weak, nonatomic) IBOutlet UILabel *lblDate;
@property (weak, nonatomic) IBOutlet UILabel *lblEval;
@property (nonatomic, weak) IBOutlet UILabel *electCountLabel;
@property (nonatomic, weak) IBOutlet UILabel *replyContentLabel;
@property (nonatomic, weak) IBOutlet UIButton *zanButton;
@property (nonatomic, weak) IBOutlet UIButton *errorButton;
@property (nonatomic, weak) IBOutlet NSLayoutConstraint *lblMoreTopSpace;

@end
